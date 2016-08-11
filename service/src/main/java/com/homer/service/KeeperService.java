package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IKeeperRepository;
import com.homer.exception.KeeperException;
import com.homer.type.DraftDollar;
import com.homer.type.DraftDollarType;
import com.homer.type.Keeper;
import com.homer.type.PlayerSeason;
import com.homer.type.history.HistoryKeeper;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 8/14/16.
 */
public class KeeperService extends BaseVersionedIdService<Keeper, HistoryKeeper> implements IKeeperService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KeeperService.class);

    private IKeeperRepository repo;
    private IDraftDollarService draftDollarService;
    private IPlayerSeasonService playerSeasonService;

    private static final int SALARY_INCREASE = 3;
    private static final int MAX_MAJOR_LEAGUE_KEEPERS = 10;
    private static final int MAX_MINOR_LEAGUE_KEEPERS = 10;
    private static final int MINIMUM_DRAFT_DOLLARS = 13;

    public KeeperService(IKeeperRepository repo, IDraftDollarService draftDollarService,
                         IPlayerSeasonService playerSeasonService) {
        super(repo);
        this.repo = repo;
        this.draftDollarService = draftDollarService;
        this.playerSeasonService = playerSeasonService;
    }

    @Override
    public List<Keeper> getKeepers(long teamId) {
        return repo.getForTeam(teamId, LeagueUtil.NEXT_SEASON);
    }

    @Override
    public List<Keeper> replaceKeepers(List<Keeper> newKeepers, long teamId) {
        int majorLeagueKeepers = $.of(newKeepers).filter(keeper -> !keeper.getIsMinorLeaguer()).toList().size();
        int minorLeagueKeepers = newKeepers.size() - majorLeagueKeepers;
        if (majorLeagueKeepers > MAX_MAJOR_LEAGUE_KEEPERS) {
            String message = "Too many major league keepers selected";
            LOGGER.error(message);
            throw new KeeperException.MajorLeagueCountExceeded(message);
        }
        if (minorLeagueKeepers > MAX_MINOR_LEAGUE_KEEPERS) {
            String message = "Too many minor league keepers selected";
            LOGGER.error(message);
            throw new KeeperException.MinorLeagueCountExceeded(message);
        }

        DraftDollar nextSeasonAvailableSalary =
                $.of(draftDollarService.getDraftDollarsByTeam(teamId))
                        .filter(dd -> dd.getSeason() == LeagueUtil.NEXT_SEASON &&
                        dd.getDraftDollarType() == DraftDollarType.MLBAUCTION).first();
        if (nextSeasonAvailableSalary == null) {
            String message = "Could not find draft dollars for next season for team " + teamId;
            LOGGER.error(message);
            throw new NullPointerException(message);
        }

        List<Long> playerIds = $.of(newKeepers).toList(Keeper::getPlayerId);
        Map<Long, PlayerSeason> currentPlayerSeasonMap = playerSeasonService.getCurrentPlayerSeasons(playerIds);
        for(Keeper keeper : newKeepers) {
            PlayerSeason currentPlayerSeason = currentPlayerSeasonMap.get(keeper.getPlayerId());
            if (currentPlayerSeason.getTeamId() != teamId) {
                String message = "A player selected as a keeper is not a member of the selected team";
                LOGGER.error(message);
                throw new KeeperException.IncorrectTeam(message);
            }
            if (!currentPlayerSeason.getIsMinorLeaguer() && keeper.getIsMinorLeaguer()) {
                String message = "A player selected as a minor leaguer is not eligible to be kept as a minor leaguer";
                LOGGER.error(message);
                throw new KeeperException.IneligibleMinorLeaguer(message);
            }
            int nextSalary = currentPlayerSeason.getSalary();
            int nextKeeperSeason = currentPlayerSeason.getKeeperSeason();
            if (!keeper.getIsMinorLeaguer()) {
                nextSalary += SALARY_INCREASE;
                nextKeeperSeason++;
            }
            if (nextKeeperSeason > 3) {
                String message = "A player selected as a keeper has no remaining keeper years available";
                LOGGER.error(message);
                throw new KeeperException.NoKeeperYearsRemaining(message);
            }
            keeper.setSalary(nextSalary);
            keeper.setKeeperSeason(nextKeeperSeason);
            keeper.setTeamId(teamId);
            keeper.setSeason(LeagueUtil.NEXT_SEASON);
        }

        int totalKeeperSalaries = $.of(newKeepers).reduceToInt(Keeper::getSalary);
        if (totalKeeperSalaries > (nextSeasonAvailableSalary.getAmount() - MINIMUM_DRAFT_DOLLARS)) {
            String message = "Not enough funds to keep all of the selected players";
            LOGGER.error(message);
            throw new KeeperException.InsufficientFunds(message);
        }

        List<Keeper> existingKeepers = repo.getForTeam(teamId, LeagueUtil.NEXT_SEASON);
        for (Keeper keeper : existingKeepers) {
            repo.delete(keeper.getId());
        }
        List<Keeper> results = Lists.newArrayList();
        for (Keeper keeper : newKeepers) {
            results.add(repo.upsert(keeper));
        }
        return results;
    }
}
