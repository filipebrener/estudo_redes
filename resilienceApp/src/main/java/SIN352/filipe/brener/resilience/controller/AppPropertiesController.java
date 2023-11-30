package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.dto.AppPropertiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appConfig")
public class AppPropertiesController {

    @Autowired
    private AppPropertiesDTO properties;

    @GetMapping
    public AppPropertiesDTO getAppProperties(){
        return properties;
    }

}
