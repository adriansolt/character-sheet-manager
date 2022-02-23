package com.adi.cms.character;

import com.adi.cms.character.CharacterApp;
import com.adi.cms.character.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CharacterApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
