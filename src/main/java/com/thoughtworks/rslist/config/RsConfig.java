package com.thoughtworks.rslist.config;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.TradeRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RsConfig {

    @Bean
    public RsEventService rsEventService(RsEventRepository rsEventRepository,
                                         VoteRepository voteRepository,
                                         UserRepository userRepository,
                                         TradeRepository tradeRepository) {

        return new RsEventService(rsEventRepository,
                voteRepository,
                userRepository,
                tradeRepository);
    }
}
