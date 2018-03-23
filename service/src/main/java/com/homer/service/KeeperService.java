package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        checkKeeperSizes(newKeepers);

        Map<Long, PlayerSeason> currentPlayerSeasonMap = currentPlayerSeasonMap(newKeepers);
        for(Keeper keeper : newKeepers) {
            PlayerSeason currentPlayerSeason = currentPlayerSeasonMap.get(keeper.getPlayerId());
            validatePlayer(teamId, currentPlayerSeason, keeper);

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

        validateSufficientFunds(getNextSeasonAvailableSalary(teamId), newKeepers);
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

    @Override
    public void deselectKeeper(long playerId) {
        Keeper keeper = repo.getByPlayerId(playerId, LeagueUtil.NEXT_SEASON);
        if (keeper != null)
        {
            repo.delete(keeper.getId());
        }
    }

    @Override
    public List<PlayerSeason> finalizeKeepers(long teamId)
    {
        List<Keeper> keepers = repo.getForTeam(teamId, LeagueUtil.NEXT_SEASON);

        checkKeeperSizes(keepers);

        Map<Long, PlayerSeason> currentPlayerSeasonMap = currentPlayerSeasonMap(keepers);
        List<PlayerSeason> newPlayerSeasons = Lists.newArrayList();
        for(Keeper keeper : keepers) {
            PlayerSeason currentPlayerSeason = currentPlayerSeasonMap.get(keeper.getPlayerId());
            validatePlayer(teamId, currentPlayerSeason, keeper);

            newPlayerSeasons.add(playerSeasonService.createPlayerSeasonForKeeper(currentPlayerSeason, keeper));
        }

        DraftDollar nextSeasonAvailableSalary = getNextSeasonAvailableSalary(teamId);
        int usedDraftDollars = validateSufficientFunds(nextSeasonAvailableSalary, keepers);
        nextSeasonAvailableSalary.setAmount(nextSeasonAvailableSalary.getAmount() - usedDraftDollars);
        draftDollarService.upsert(nextSeasonAvailableSalary);

        return $.of(newPlayerSeasons).toList(playerSeasonService::upsert);
    }

    // region validation

    private Map<Long, PlayerSeason> currentPlayerSeasonMap(Collection<Keeper> keepers)
    {
        List<Long> playerIds = $.of(keepers).toList(Keeper::getPlayerId);
        Map<Long, PlayerSeason> currentPlayerSeasonMap = Maps.newHashMap();
        for(long playerId : playerIds) {
            PlayerSeason currentPlayerSeason = playerSeasonService.getCurrentPlayerSeason(playerId);
            currentPlayerSeasonMap.put(playerId, currentPlayerSeason);
        }
        return currentPlayerSeasonMap;
    }

    private static void checkKeeperSizes(Collection<Keeper> keepers)
    {
        int majorLeagueKeepers = $.of(keepers).filter(keeper -> !keeper.getIsMinorLeaguer()).toList().size();
        int minorLeagueKeepers = keepers.size() - majorLeagueKeepers;
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
    }

    private static void validatePlayer(long teamId, PlayerSeason playerSeason, Keeper keeper)
    {
        if (!Objects.equals(playerSeason.getTeamId(), teamId)) {
            String message = "A player selected as a keeper is not a member of the selected team";
            LOGGER.error(message);
            throw new KeeperException.IncorrectTeam(message);
        }
        if (!playerSeason.getHasRookieStatus() && keeper.getIsMinorLeaguer()) {
            String message = "A player selected as a minor leaguer no longer has rookie status and can only be kept as a major leaguer";
            LOGGER.error(message);
            throw new KeeperException.IneligibleMinorLeaguer(message);
        }
    }

    private static int validateSufficientFunds(DraftDollar nextSeasonAvailableSalary, Collection<Keeper> keepers) {
        int totalKeeperSalaries = $.of(keepers).reduceToInt(Keeper::getSalary);
        if (totalKeeperSalaries > (nextSeasonAvailableSalary.getAmount() - MINIMUM_DRAFT_DOLLARS)) {
            String message = "Not enough funds to keep all of the selected players";
            LOGGER.error(message);
            throw new KeeperException.InsufficientFunds(message);
        }
        return totalKeeperSalaries;
    }

    private DraftDollar getNextSeasonAvailableSalary(long teamId)
    {
        DraftDollar nextSeasonAvailableSalary = $.of(draftDollarService.getDraftDollarsByTeam(teamId))
                .filter(dd -> dd.getSeason() == LeagueUtil.NEXT_SEASON &&
                        dd.getDraftDollarType() == DraftDollarType.MLBAUCTION).first();
        if (nextSeasonAvailableSalary == null) {
            String message = "Could not find draft dollars for next season for team " + teamId;
            LOGGER.error(message);
            throw new NullPointerException(message);
        }
        return nextSeasonAvailableSalary;
    }

    // endregion
}
