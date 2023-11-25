package com.thybak.bots.kkbot.config;

import com.thybak.bots.kkbot.domain.provider.SecretionRepository;
import com.thybak.bots.kkbot.domain.usecase.GetSecretionRankingUseCase;
import com.thybak.bots.kkbot.domain.usecase.RegisterSecretionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZoneId;

@Component
public class KkBotConfiguration {
    private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of(SPAIN_LOCAL_ZONE));
    }

    @Bean
    public GetSecretionRankingUseCase getSecretionRankingUseCase(final Clock clock, final SecretionRepository secretionRepository) {
        return new GetSecretionRankingUseCase(clock, secretionRepository);
    }

    @Bean
    public RegisterSecretionUseCase getRegisterSecretionUseCase(final SecretionRepository secretionRepository) {
        return new RegisterSecretionUseCase(secretionRepository);
    }


}
