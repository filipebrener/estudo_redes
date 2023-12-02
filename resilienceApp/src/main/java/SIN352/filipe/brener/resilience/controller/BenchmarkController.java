package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.dto.BenchmarkDto;
import SIN352.filipe.brener.resilience.services.BenchmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/benchmark")
public class BenchmarkController {

    @Autowired
    private BenchmarkService benchmarkService;

    @GetMapping
    public ResponseEntity<List<BenchmarkDto>> bench(@RequestParam(name = "lowerCase", defaultValue = "false") boolean lowerCase,
                                                    @RequestParam(name = "threads" , defaultValue = "25,50,100") String threads,
                                                    @RequestParam(name = "failRates" , defaultValue = "0.0,0.25,0.5") String serverFailRates){

        int[] numThreadsList = Arrays.stream(threads.split(",")).mapToInt(Integer::parseInt).toArray();
        Float[] serverFailRatesList = Arrays.stream(serverFailRates.split(",")).map(Float::parseFloat).toArray(Float[]::new);

        return new ResponseEntity<>(benchmarkService.startBench(lowerCase, numThreadsList, serverFailRatesList), HttpStatusCode.valueOf(200));
    }

}
