/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.bootstrap.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author Roberto Díaz
 */
@Component
@ConfigurationProperties(prefix = "cartai.bootstrap")
@Data
public class BootstrapProperties {
    private boolean enabled;
    private Map<String, Set<String>> roles;
}
