/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Roberto Díaz
 */
@EnableScheduling
@SpringBootApplication
public class CartAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartAIApplication.class, args);
    }

}
