package com.homer.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.type.*;
import com.homer.util.HomerBeanUtil;
import com.homer.util.LeagueUtil;
import com.homer.util.core.IBaseObject;
import com.homer.util.core.IHistoryObject;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * Created by arigolub on 3/15/16.
 */
public class RepositoryTests {

    @Test
    public void testPlayerCRUD() throws Exception {
        Player player = new Player();
        player.setName("Ari Golub");
        player.setFirstName("Ari");
        player.setLastName("Golub");
        player.setPosition(Position.SHORTSTOP);
        player.setMlbTeamId(MLBTeam.NEWYORKYANKEES.getId());

        List<Consumer<Player>> funcs = Lists.newArrayList();
        funcs.add(p -> p.setPosition(Position.OUTFIELD));

        PlayerRepository repo = new PlayerRepository();
        testCRUD(player, repo, funcs);
    }

    @Test
    public void testPlayerSearch() throws Exception {
        String name = "Mike Trout";
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("name", name);
        PlayerRepository repo = new PlayerRepository();
        Player player = repo.get(filters);
        assertNotNull(player);
        assertEquals(name, player.getName());
    }

    @Test
    public void testPlayerSeasonCRUD() throws Exception {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setSeason(2020);
        playerSeason.setPlayerId(1);
        playerSeason.setTeamId(1L);
        playerSeason.setFantasyPosition(Position.CATCHER);
        playerSeason.setMlbStatus(Status.ACTIVE);
        playerSeason.setKeeperTeamId(1L);
        playerSeason.setKeeperSeason(2020);
        playerSeason.setSalary(20);
        playerSeason.setIsMinorLeaguer(true);

        List<Consumer<PlayerSeason>> funcs = Lists.newArrayList();
        funcs.add(p -> p.setTeamId(2L));
        funcs.add(p -> p.setFantasyPosition(Position.FIRSTBASE));
        funcs.add(p -> p.setIsMinorLeaguer(false));
        funcs.add(p -> p.setMlbStatus(Status.DISABLEDLIST));

        testVersionedCRUD(playerSeason, new PlayerSeasonRepository(), funcs);
    }

    @Test
    public void testDraftDollarRepositoryCRUD() throws Exception {
        DraftDollar draftDollar = new DraftDollar();
        draftDollar.setSeason(2020);
        draftDollar.setTeamId(1);
        draftDollar.setAmount(100);
        draftDollar.setDraftDollarType(DraftDollarType.MLBAUCTION);

        List<Consumer<DraftDollar>> funcs = Lists.newArrayList();
        funcs.add(dd -> dd.setAmount(200));

        testVersionedCRUD(draftDollar, new DraftDollarRepository(), funcs);
    }

    @Test
    public void testMinorLeaguePickRepositoryCRUD() throws Exception {
        MinorLeaguePick minorLeaguePick = new MinorLeaguePick();
        minorLeaguePick.setOriginalTeamId(1);
        minorLeaguePick.setOwningTeamId(1);
        minorLeaguePick.setSeason(2020);
        minorLeaguePick.setRound(1);
        minorLeaguePick.setIsSkipped(false);

        List<Consumer<MinorLeaguePick>> funcs = Lists.newArrayList();
        funcs.add(mlp -> mlp.setOwningTeamId(2));
        funcs.add(mlp -> mlp.setSwapTeamId(3L));
        funcs.add(mlp -> mlp.setPlayerId(1L));
        funcs.add(mlp -> mlp.setIsSkipped(false));
        funcs.add(mlp -> mlp.setNote("Swap with the other team"));

        testCRUD(minorLeaguePick, new MinorLeaguePickRepository(), funcs);
    }

    @Test
    public void testTradeCRUD() throws Exception {
        Trade trade = new Trade();
        trade.setTeam1Id(1);
        trade.setTeam2Id(2);
        trade.setProposedDateUTC(DateTime.now().withMillisOfSecond(0));

        List<Consumer<Trade>> funcs = Lists.newArrayList();

        trade = testCRU(trade, new TradeRepository(), funcs);

        TradeElement tradeElement = new TradeElement();
        tradeElement.setTradeId(trade.getId());
        tradeElement.setTeamFromId(trade.getTeam1Id());
        tradeElement.setTeamToId(trade.getTeam2Id());

        TradeElement tradeElement2 = new TradeElement();
        HomerBeanUtil.copyProperties(tradeElement2, tradeElement);

        tradeElement.setPlayerId(1L);
        tradeElement2.setDraftDollarId(1L);
        tradeElement2.setDraftDollarAmount(10);

        TradeElementRepository tradeElementRepository = new TradeElementRepository();
        testCRU(tradeElement, tradeElementRepository, Lists.newArrayList());
        testCRU(tradeElement2, tradeElementRepository, Lists.newArrayList());
    }

    @Test
    public void testVultureCRUD() throws Exception {
        Vulture vulture = new Vulture();
        vulture.setTeamId(1);
        vulture.setPlayerId(1);
        vulture.setDropPlayerId(2L);
        vulture.setDeadlineUTC(DateTime.now().withMillisOfSecond(0));
        vulture.setVultureStatus(EventStatus.IN_PROGRESS);

        List<Consumer<Vulture>> funcs = Lists.newArrayList(v -> v.setVultureStatus(EventStatus.COMPLETE));
        testVersionedCRUD(vulture, new VultureRepository(), funcs);
    }

    @Test
    public void testFreeAgentAuctionCRUD() throws Exception {
        FreeAgentAuction freeAgentAuction = new FreeAgentAuction();
        freeAgentAuction.setPlayerId(1L);
        freeAgentAuction.setDeadlineUTC(DateTime.now().withMillisOfSecond(0));
        freeAgentAuction.setAuctionStatus(EventStatus.IN_PROGRESS);
        freeAgentAuction.setSeason(LeagueUtil.SEASON);
        freeAgentAuction.setRequestingTeamId(1);

        List<Consumer<FreeAgentAuction>> funcs = Lists.newArrayList(faa -> {
            faa.setAuctionStatus(EventStatus.COMPLETE);
            faa.setWinningTeamId(1L);
            faa.setWinningAmount(100);
        });

        freeAgentAuction = testCRU(freeAgentAuction, new FreeAgentAuctionRepository(), funcs);

        FreeAgentAuctionBid freeAgentAuctionBid = new FreeAgentAuctionBid();
        freeAgentAuctionBid.setFreeAgentAuctionId(freeAgentAuction.getId());
        freeAgentAuctionBid.setTeamId(1);

        List<Consumer<FreeAgentAuctionBid>> funcs2 = Lists.newArrayList(faab -> faab.setAmount(100));

        testVersionedCRUD(freeAgentAuctionBid, new FreeAgentAuctionBidRepository(), funcs2);
    }

    @Test
    public void testTransactionCRUD() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setPlayerId(1);
        transaction.setTransactionDate(DateTime.now().withMillisOfSecond(0));
        transaction.setTeamId(1);
        transaction.setOldPosition(Position.PITCHER);
        transaction.setNewPosition(Position.MIDDLEINFIELD);
        transaction.setTransactionType(TransactionType.ADD);
        transaction.setText("Transaction Text");

        List<Consumer<Transaction>> updaters = Lists.newArrayList();
        testCRUD(transaction, new TransactionRepository(), updaters);
    }

    @Test
    public void testPlayerDailyCRUD() throws Exception {
        PlayerDaily playerDaily = new PlayerDaily();
        playerDaily.setPlayerId(1);
        playerDaily.setTeamId(1L);
        playerDaily.setGameId("2016/06/07/nynmlb-pitmlb-2");
        playerDaily.setDate(DateTime.parse("2016-04-03T12:00:00").withMillisOfDay(0));
        playerDaily.setScoringPeriodId(1);

        Consumer<PlayerDaily> updater = (pd) -> {
            pd.setId(3);
            pd.setAtBats(4);
            pd.setInningsPitched(3.33);
        };
        PlayerDailyRepository repo = new PlayerDailyRepository();
        testCRU(playerDaily, repo, updated -> repo.getByKey(updated.getPlayerId(), updated.getDate()), new ArrayList<Consumer<PlayerDaily>>(Lists.newArrayList(updater)));
    }

    @Test
    public void testTeamDailyCRUD() throws Exception {
        TeamDaily teamDaily = new TeamDaily();
        teamDaily.setTeamId(1L);
        teamDaily.setDate(DateTime.parse("2016-04-03T12:00:00").withMillisOfDay(0));
        teamDaily.setScoringPeriodId(1);

        Consumer<TeamDaily> updater = (pd) -> {
            pd.setId(3);
            pd.setAtBats(4);
            pd.setInningsPitched(3.33);
        };
        TeamDailyRepository repo = new TeamDailyRepository();
        testCRU(teamDaily, repo, updated -> repo.getByKey(updated.getTeamId(), updated.getDate()), new ArrayList<Consumer<TeamDaily>>(Lists.newArrayList(updater)));
    }

    @Test
    public void testStandingsCRUD() throws Exception {
        Standing standing = new Standing();
        standing.setTeamId(1);
        standing.setDate(DateTime.parse("2016-04-03T12:00:00").withMillisOfDay(0));

        Consumer<Standing> updater = (s) -> {
            s.setEraPoints(10.3);
            s.setEraTotal(2.0);
            s.setSbPoints(3.4);
            s.setSbTotal(4);
            s.setkTotal(100);
        };

        StandingRepository repo = new StandingRepository();
        testCRU(standing, repo, updated -> repo.getByKey(updated.getTeamId(), updated.getDate()), new ArrayList<Consumer<Standing>>(Lists.newArrayList(updater)));
    }

    private <T extends IBaseObject> void testCRUD(T obj, IRepository<T> repo,
                                                    List<Consumer<T>> updaters) throws Exception {
        T updatedObj = testCRU(obj, repo, updaters);

        assertTrue(repo.deleteNoHistory(updatedObj));
        T deletedObj = repo.getById(updatedObj.getId());
        assertNull(deletedObj);
    }

    private <T extends IBaseObject> T testCRU(T obj, IRepository<T> repo,
                                              List<Consumer<T>> updaters) throws Exception {
        return testCRU(obj, repo, (updated) -> repo.getById(updated.getId()), updaters);
    }

    private <T extends IBaseObject> T testCRU(T obj, IRepository<T> repo, Function<T, T> supplier,
                                                  List<Consumer<T>> updaters) throws Exception {
        T createdObj = repo.upsertNoHistory(obj);
        assertNotNull(createdObj);
        obj.setId(createdObj.getId());
        assertEquals(obj, createdObj);

        T fetchedObj = supplier.apply(createdObj);
        assertEquals(createdObj, fetchedObj);

        updaters.forEach(u -> u.accept(createdObj));
        T updatedObj = repo.upsertNoHistory(createdObj);
        assertNotNull(updatedObj);
        assertEquals(createdObj, updatedObj);

        return updatedObj;
    }

    private <T extends IBaseObject, H extends IHistoryObject> void testVersionedCRUD(T obj, IVersionedRepository<T, H> repo,
                                                  List<Consumer<T>> updaters) throws Exception {
        T updatedObj = testVersionedCRU(obj, repo, (updated) -> repo.getById(updated.getId()), updaters);

        assertTrue(repo.delete(updatedObj));
        T deletedObj = repo.getById(updatedObj.getId());
        assertNull(deletedObj);

        List<H> historyObjects = repo.getHistories(updatedObj.getId());
        assertEquals(3, historyObjects.size());
        assertTrue(historyObjects.get(2).getIsDeleted());
    }

    private <T extends IBaseObject, H extends IHistoryObject> T testVersionedCRU(T obj, IVersionedRepository<T, H> repo, Function<T, T> supplier,
                                                                                 List<Consumer<T>> updaters) throws Exception {
        T createdObj = repo.upsert(obj);
        assertNotNull(createdObj);
        obj.setId(createdObj.getId());
        assertEquals(obj, createdObj);

        T fetchedObj = supplier.apply(createdObj);
        assertEquals(createdObj, fetchedObj);

        updaters.forEach(u -> u.accept(createdObj));
        T updatedObj = repo.upsert(createdObj);
        assertNotNull(updatedObj);
        assertEquals(createdObj, updatedObj);

        List<H> historyObjects = repo.getHistories(createdObj.getId());
        assertEquals(2, historyObjects.size());

        return updatedObj;
    }
}
