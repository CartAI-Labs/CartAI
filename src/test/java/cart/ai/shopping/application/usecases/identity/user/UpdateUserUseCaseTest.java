/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.domain.model.identity.vos.RoleId;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.identity.UserUpdatedEventPublisherPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static cart.ai.shopping.domain.common.result.ResultError.CONFLICT;
import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UpdateUserUseCase.
 *
 * @author Roberto Díaz
 */
@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    private static final Role CUSTOMER_ROLE = new Role(new RoleId("1"), "CUSTOMER", Set.of(new Permission("ROLE_CUSTOMER")));
    private static final UserId USER_ID = new UserId("1001");
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private UserUpdatedEventPublisherPort userUpdatedEventPublisherPort;
    @Mock
    private StoragePort storagePort;
    @Mock
    private TempStoragePort tempStoragePort;
    private UpdateUserUseCase updateUserUseCase;

    @BeforeEach
    void setUp() {
        updateUserUseCase = new UpdateUserUseCase(
                userRepositoryPort, userUpdatedEventPublisherPort, storagePort, tempStoragePort
        );
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(null);

        UpdateUserCommand command = new UpdateUserCommand("1001", "John", "john@test.com", Set.of(CUSTOMER_ROLE), null);

        Result<User> result = updateUserUseCase.execute(command);

        assertTrue(result.hasError());
        assertEquals(NOT_FOUND, result.getError());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void shouldReturnConflictWhenEmailTakenByAnotherUser() {
        User existingUser = new User(USER_ID, "John", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE));
        User anotherUser = new User(new UserId("9999"), "Other", new Email("taken@test.com"), "hash", Set.of(CUSTOMER_ROLE));
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(existingUser);
        when(userRepositoryPort.findByEmail(new Email("taken@test.com"))).thenReturn(anotherUser);

        UpdateUserCommand command = new UpdateUserCommand("1001", "John", "taken@test.com", Set.of(CUSTOMER_ROLE), null);

        Result<User> result = updateUserUseCase.execute(command);

        assertTrue(result.hasError());
        assertEquals(CONFLICT, result.getError());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void shouldUpdateUserWithoutChangingAvatar() {
        User existingUser = new User(USER_ID, "John", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE), "existing-avatar.jpg");
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(existingUser);
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Same avatarFileId as the existing one — no promotion should occur
        UpdateUserCommand command = new UpdateUserCommand("1001", "John Updated", "john@test.com", Set.of(CUSTOMER_ROLE), "existing-avatar.jpg");

        Result<User> result = updateUserUseCase.execute(command);

        assertFalse(result.hasError());
        assertEquals("John Updated", result.getValue().name());
        assertEquals("existing-avatar.jpg", result.getValue().avatarFileId());
        verify(storagePort, never()).promoteFile(any(), any());
        verify(storagePort, never()).deleteFile(any());
    }

    @Test
    void shouldPromoteNewAvatarAndDeleteOldOne() {
        User existingUser = new User(USER_ID, "John", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE), "old-avatar.jpg");
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(existingUser);
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(tempStoragePort.getBucketName()).thenReturn("test-temp-bucket");

        UpdateUserCommand command = new UpdateUserCommand("1001", "John", "john@test.com", Set.of(CUSTOMER_ROLE), "new-avatar.jpg");

        Result<User> result = updateUserUseCase.execute(command);

        assertFalse(result.hasError());
        assertEquals("new-avatar.jpg", result.getValue().avatarFileId());
        verify(storagePort).promoteFile("new-avatar.jpg", "test-temp-bucket");
        verify(storagePort).deleteFile("old-avatar.jpg");
    }

    @Test
    void shouldKeepExistingAvatarWhenNoNewAvatarProvided() {
        User existingUser = new User(USER_ID, "John", new Email("john@test.com"), "hash", Set.of(CUSTOMER_ROLE), "existing-avatar.jpg");
        when(userRepositoryPort.findByUserId(USER_ID)).thenReturn(existingUser);
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserCommand command = new UpdateUserCommand("1001", "John Updated", "john@test.com", Set.of(CUSTOMER_ROLE), null);

        Result<User> result = updateUserUseCase.execute(command);

        assertFalse(result.hasError());
        assertEquals("existing-avatar.jpg", result.getValue().avatarFileId());
        verify(storagePort, never()).promoteFile(any(), any());
        verify(storagePort, never()).deleteFile(any());
    }
}
