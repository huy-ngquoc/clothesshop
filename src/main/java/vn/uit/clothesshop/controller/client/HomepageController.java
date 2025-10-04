package vn.uit.clothesshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;

@Controller
public class HomepageController {
    @GetMapping("/")
    public String getHomepage(final Model model) {
        return "client/homepage/show";
    }
}
