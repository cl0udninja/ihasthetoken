package com.jelohazi.ihasthetoken.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Token implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6013760174426388338L;

    private UUID id;
    private UUID userId;

    private Date created;
    private TokenStatus status;

    public Token() {
    }

    public Token(UUID id, UUID userId, Date created, TokenStatus status) {
        super();
        this.id = id;
        this.userId = userId;
        this.created = created;
        this.status = status;
    }

    public Token(UUID id, UUID userId, Date created, String status) {
        super();
        this.id = id;
        this.userId = userId;
        this.created = created;
        this.status = TokenStatus.valueOf(status);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        validateStatus(status);
        this.status = status;
    }

    public void setStatus(String status) {
        validateStatus(TokenStatus.valueOf(status));
        this.status = TokenStatus.valueOf(status);
    }

    private void validateStatus(TokenStatus newStatus) {
        if (this.status == TokenStatus.CREATED && newStatus != TokenStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("CREATED token can only be changed to IN_PROGRESS");
        }
        if (this.status == TokenStatus.IN_PROGRESS && newStatus != TokenStatus.RELEASED) {
            throw new IllegalArgumentException("IN_PROGRESS token can only be changed to RELEASED");
        }
    }

    @Override
    public String toString() {
        return "Token [id=" + id + ", userId=" + userId + ", created=" + created + ", status=" + status + "]";
    }

}