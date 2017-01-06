package com.jelohazi.ihasthetoken.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jelohazi.ihasthetoken.domain.Token;
import com.jelohazi.ihasthetoken.domain.TokenStatus;
import com.jelohazi.ihasthetoken.jdbc.TokenRepository;

@Controller
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenRepository tokenRepository;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Token> get(
            @RequestParam(value = "id", required = false) String id) {
        if (id == null) {
            return tokenRepository.list();
        }
        Token t = tokenRepository.get(UUID.fromString(id));
        return Arrays.asList(new Token[] { t });
    }

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestBody Token token) {
        if (token.getId() == null) {
            assert (token.getUserId() != null);
            TokenStatus ts = tokenRepository.hasInProgressToken() ? TokenStatus.CREATED : TokenStatus.IN_PROGRESS;
            tokenRepository
                    .createOrUpdate(new Token(UUID.randomUUID(), token.getUserId(), new Date(), ts));
            return;
        }
        assert (token.getId() != null && token.getStatus() != null);
        Token t = tokenRepository.get(token.getId());
        TokenStatus before = t.getStatus();
        t.setStatus(token.getStatus());
        tokenRepository.createOrUpdate(t);
    }
}
