package com.bansal.yash.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bansal.yash.models.LocationStats;
import com.bansal.yash.service.CVDataService;

/**
 * @author bansalyash
 *
 */
@Controller
@RequestMapping
public class HomeController {

	@Autowired
	CVDataService cvDataService;

	@GetMapping("/")
	public String indexPage(Model model) {
		List<LocationStats> allStats = cvDataService.getAllStats();
		int totalReportedCases = allStats.stream().mapToInt(Stat -> Stat.getLatestTotalCases()).sum();
		int totalNewCases = allStats.stream().mapToInt(Stat -> Stat.getDiffFromPrevDay()).sum();
		model.addAttribute("locationStats", allStats);
		model.addAttribute("totalReportedCases", totalReportedCases);
		model.addAttribute("totalNewCases", totalNewCases);
		return "home";
	}

}
