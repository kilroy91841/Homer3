package com.homer.web;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.data.common.*;
import com.homer.email.IEmailService;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.espn.IESPNClient;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.*;
import com.homer.service.auth.IAuthService;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.UserService;
import com.homer.service.eventbus.PlayerSeasonChangeEventRecorder;
import com.homer.service.full.*;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.service.importer.IPlayerImporter;
import com.homer.service.importer.PlayerImporter;
import com.homer.service.schedule.IScheduler;
import com.homer.service.schedule.Scheduler;

import java.util.Map;

/**
 * Created by arigolub on 7/31/16.
 */
public final class ServiceFactory {

    private static Map<Class<?>, Object> instanceMap = Maps.newHashMap();

    private ServiceFactory() {
        initialize();
    }

    private void initialize() {
        instanceMap.put(EventBus.class, new EventBus());

        instanceMap.put(ITeamRepository.class, new TeamRepository());
        instanceMap.put(ITeamService.class, new TeamService(get(ITeamRepository.class)));

        instanceMap.put(IPlayerRepository.class, new PlayerRepository());
        instanceMap.put(IPlayerService.class, new PlayerService(get(IPlayerRepository.class)));

        instanceMap.put(IPlayerSeasonRepository.class, new PlayerSeasonRepository());
        instanceMap.put(IPlayerSeasonService.class, new PlayerSeasonService(get(IPlayerSeasonRepository.class), get(EventBus.class)));

        instanceMap.put(IDraftDollarRepository.class, new DraftDollarRepository());
        instanceMap.put(IDraftDollarService.class, new DraftDollarService(get(IDraftDollarRepository.class)));

        instanceMap.put(IMinorLeaguePickRepository.class, new MinorLeaguePickRepository());
        instanceMap.put(IMinorLeaguePickService.class, new MinorLeaguePickService(get(IMinorLeaguePickRepository.class)));

        instanceMap.put(ITradeRepository.class, new TradeRepository());
        instanceMap.put(ITradeService.class, new TradeService(get(ITradeRepository.class)));

        instanceMap.put(ITradeElementRepository.class, new TradeElementRepository());
        instanceMap.put(ITradeElementService.class, new TradeElementService(get(ITradeElementRepository.class)));

        instanceMap.put(IFreeAgentAuctionRepository.class, new FreeAgentAuctionRepository());
        instanceMap.put(IFreeAgentAuctionService.class, new FreeAgentAuctionService(get(IFreeAgentAuctionRepository.class)));

        instanceMap.put(IFreeAgentAuctionBidRepository.class, new FreeAgentAuctionBidRepository());
        instanceMap.put(IFreeAgentAuctionBidService.class, new FreeAgentAuctionBidService(get(IFreeAgentAuctionBidRepository.class)));

        instanceMap.put(ISessionTokenRepository.class, new SessionTokenRepository());
        instanceMap.put(IAuthService.class, StormpathAuthService.FACTORY.getInstance());
        instanceMap.put(IUserService.class, new UserService(get(IAuthService.class), get(ISessionTokenRepository.class)));
        instanceMap.put(IEmailService.class, new AWSEmailService());

        instanceMap.put(IFullTeamService.class, new FullTeamService(get(IPlayerSeasonService.class), get(ITeamService.class),
                get(IUserService.class), get(IEmailService.class)));

        instanceMap.put(IScheduler.class, new Scheduler());

        instanceMap.put(IGatherer.class, new Gatherer(
                get(IPlayerService.class),
                get(ITeamService.class),
                get(IPlayerSeasonService.class),
                get(IDraftDollarService.class),
                get(IMinorLeaguePickService.class),
                get(ITradeService.class),
                get(ITradeElementService.class)
        ));

        instanceMap.put(Validator.class, new Validator(get(ITeamService.class), get(IGatherer.class), get(IEmailService.class)));

        instanceMap.put(IFullTradeService.class, new FullTradeService(get(ITradeService.class), get(IMinorLeaguePickService.class),
                get(IDraftDollarService.class), get(IPlayerSeasonService.class), get(ITradeElementService.class), get(IGatherer.class),
                get(IEmailService.class), get(IUserService.class)));
        instanceMap.put(IMLBClient.class, new MLBRestClient());

        instanceMap.put(IPlayerImporter.class, new PlayerImporter(get(IPlayerService.class), get(IPlayerSeasonService.class),
                get(IMLBClient.class)));

        instanceMap.put(IFullPlayerService.class, new FullPlayerService(
                get(IPlayerService.class),
                get(IPlayerSeasonService.class),
                get(IMLBClient.class)
        ));

        instanceMap.put(IFullMinorLeagueDraftService.class, new FullMinorLeagueDraftService(
                get(IGatherer.class),
                get(IMinorLeaguePickService.class),
                get(IPlayerService.class),
                get(IPlayerSeasonService.class),
                get(IFullPlayerService.class),
                get(IMLBClient.class),
                get(IEmailService.class),
                get(IUserService.class),
                get(ITeamService.class),
                get(IScheduler.class)
        ));

        instanceMap.put(IESPNClient.class, new ESPNRestClient());

        instanceMap.put(ITransactionRepository.class, new TransactionRepository());
        instanceMap.put(ITransactionService.class, new TransactionService(
                get(ITransactionRepository.class),
                get(IPlayerService.class),
                get(IPlayerSeasonService.class),
                get(IESPNClient.class),
                get(IEmailService.class)));

        instanceMap.put(IPlayerDailyRepository.class, new PlayerDailyRepository());
        instanceMap.put(ITeamDailyRepository.class, new TeamDailyRepository());
        instanceMap.put(IStandingRepository.class, new StandingRepository());
        instanceMap.put(ISeptemberStandingRepository.class, new SeptemberStandingRepository());

        instanceMap.put(IPlayerDailyService.class, new PlayerDailyService(
                get(IPlayerDailyRepository.class),
                get(IPlayerService.class),
                get(IESPNClient.class),
                get(IMLBClient.class),
                get(ITeamService.class),
                get(IEmailService.class)
        ));
        instanceMap.put(ITeamDailyService.class, new TeamDailyService(
                get(ITeamDailyRepository.class),
                get(IPlayerDailyService.class),
                get(ITeamService.class)
        ));
        instanceMap.put(IStandingService.class, new StandingService(
                get(IStandingRepository.class),
                get(ITeamDailyService.class),
                get(IPlayerDailyService.class),
                get(ISeptemberStandingRepository.class),
                get(IDraftDollarService.class)
        ));

        instanceMap.put(IVultureRepository.class, new VultureRepository());
        instanceMap.put(IVultureService.class, new VultureService(
                get(IVultureRepository.class)
        ));
        instanceMap.put(IFullVultureService.class, new FullVultureService(
                get(IVultureService.class),
                get(IPlayerSeasonService.class),
                get(ITeamService.class),
                get(IPlayerService.class),
                get(IUserService.class),
                get(IEmailService.class),
                get(IScheduler.class)
        ));

        instanceMap.put(IFullHistoryService.class, new FullHistoryService(
                get(IDraftDollarService.class),
                get(IFullTradeService.class),
                get(ITeamService.class),
                get(IStandingService.class)
        ));

        instanceMap.put(IKeeperRepository.class, new KeeperRepository());
        instanceMap.put(IKeeperService.class, new KeeperService(
                get(IKeeperRepository.class),
                get(IDraftDollarService.class),
                get(IPlayerSeasonService.class)));

        instanceMap.put(IFullFreeAgentAuctionService.class, new FullFreeAgentAuctionService(
                get(IFreeAgentAuctionService.class),
                get(IFreeAgentAuctionBidService.class),
                get(IPlayerSeasonService.class),
                get(IPlayerService.class),
                get(IFullPlayerService.class),
                get(IEmailService.class),
                get(IDraftDollarService.class),
                get(IMLBClient.class),
                get(ITeamService.class),
                get(IScheduler.class),
                get(IUserService.class)
        ));

        instanceMap.put(PlayerSeasonChangeEventRecorder.class, new PlayerSeasonChangeEventRecorder(get(IKeeperService.class)));

        registerEventBus();
    }

    private void registerEventBus() {
        EventBus eventBus = get(EventBus.class);
        eventBus.register(get(PlayerSeasonChangeEventRecorder.class));
    }

    public <T> T get(Class<T> clazz) {
        Object obj = instanceMap.get(clazz);
        if (obj == null) {
            throw new RuntimeException("No instance of " + clazz + " found in the map");
        }
        return (T) obj;
    }

    private static ServiceFactory instance;

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }
}
