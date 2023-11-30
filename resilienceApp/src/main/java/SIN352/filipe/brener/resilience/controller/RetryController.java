package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.services.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/retry")
public class RetryController {

    @Autowired
    private RetryService retryService;

    @GetMapping("/framework")
    public String framework_retry() throws IOException, InterruptedException {
        return retryService.frameworkRetry();
    }

    @GetMapping("/implemented")
    public String retryManually() throws InterruptedException {
        return retryService.implementedRetry();
    }

}
