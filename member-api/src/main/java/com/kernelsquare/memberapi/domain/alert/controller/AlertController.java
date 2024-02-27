package com.kernelsquare.memberapi.domain.alert.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.alert.dto.AlertDto;
import com.kernelsquare.memberapi.domain.alert.facade.AlertFacade;
import com.kernelsquare.memberapi.domain.alert.manager.SseManager;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.kernelsquare.core.common_response.response.code.AlertResponseCode.MY_ALERT_ALL_FOUND;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AlertController {
    private final AlertFacade alertFacade;
    private final SseManager sseManager;

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<AlertDto.FindAllResponse>>> findAllAlerts(
        @AuthenticationPrincipal
        MemberAdapter memberAdapter
    ) {
        List<AlertDto.FindAllResponse> response = alertFacade.findAllAlerts(memberAdapter);

        return ResponseEntityFactory.toResponseEntity(MY_ALERT_ALL_FOUND, response);
    }

    @GetMapping(value = "/alerts/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
        @AuthenticationPrincipal
        MemberAdapter memberAdapter
    ) {
        return ResponseEntity.ok(sseManager.subscribe(memberAdapter));
    }

}
