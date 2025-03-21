package africa.springCore.martbackend.core.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/health-check")
@RestController
@RequiredArgsConstructor
@Slf4j
public class HealthCheck {

    @GetMapping("")
    public ResponseEntity<String> checkHealth(){
        log.info("Server is being called");
        return ResponseEntity.ok("I'm up and running!!!");
    }
}
