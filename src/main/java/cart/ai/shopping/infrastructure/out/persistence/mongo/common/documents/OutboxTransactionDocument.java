/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Roberto Díaz
 */
@Builder
@Data
@Document("outbox_transaction")
public class OutboxTransactionDocument {
    public static final int PENDING = 0;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final int PROCESSING = 3;
    public static final int DEAD = 4;
    public static final int MAX_RETRIES = 5;
    @Id
    private final String id;
    private String aggregateType;
    private String aggregateId;
    private String key;
    private String topic;
    private String payload;
    private int status;
    private int retryCount;
    private Date createdDate;
    private Date lastAttemptDate;
}
