package me.mkhwang.springsecuritytest1.form;

import me.mkhwang.springsecuritytest1.account.AccountContext;
import me.mkhwang.springsecuritytest1.account.AccountRepository;
import me.mkhwang.springsecuritytest1.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.concurrent.Callable;

@Controller
public class SampleController {

    @Autowired
    SampleService sampleService;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/")
    public String index(Model model, Principal principal){

        if(principal == null){
            model.addAttribute("message", "Hello SpringSecurity");
        }else{
            model.addAttribute("message", "Hello "+principal.getName());
        }

        return "index";
    }
    @GetMapping("/info")
    public String info(Model model){
        model.addAttribute("message", "Hello info");
        return "info";
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("message", "Hello "+ principal.getName());
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName()));
        sampleService.dashboard();
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal){
        model.addAttribute("message", "Hello " + principal.getName());
        return "admin";
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal){
        model.addAttribute("message", "Hello " + principal.getName());
        return "user";
    }

    /**
     * WebaSyncManagerIntegrationFilter의 기능
     * 다른 thread에서도 동일한 SecurityContext를 사용할 수 있도록
     * @return
     */
    @GetMapping("/async-handler")
    @ResponseBody
    public Callable<String> asyncHandler() {
        SecurityLogger.log("MVC");

        return () -> {
            SecurityLogger.log("Callable");
            return "Async Handler";
        };
    }

    @GetMapping("/async-service")
    @ResponseBody
    public String asyncService() {
        SecurityLogger.log("MVC, before async service");
        sampleService.ayncService();
        SecurityLogger.log("MVC, after async service");
        return "Async Service";
    }
}
