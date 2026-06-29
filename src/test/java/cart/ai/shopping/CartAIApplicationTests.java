/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping;

import cart.ai.shopping.infrastructure.config.TestStorageConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * @author Roberto Díaz
 */
@SpringBootTest
@Import(TestStorageConfig.class)
class CartAIApplicationTests {

    @Test
    void contextLoads() {
    }

}
