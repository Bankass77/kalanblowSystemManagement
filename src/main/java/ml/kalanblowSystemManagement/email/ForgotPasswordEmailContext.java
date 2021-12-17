package ml.kalanblowSystemManagement.email;

import org.springframework.web.util.UriComponentsBuilder;

import ml.kalanblowSystemManagement.dto.model.UserDto;

public class ForgotPasswordEmailContext extends AbstractEmailContext{
	
	private String token;
	
	@Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        UserDto customer = (UserDto) context; // we pass the customer informati
        put("firstName", customer.getFullName());
        setTemplateLocation("emails/forgot-password");
        setSubject("Forgotten Password");
        setFrom("no-reply@javadevjournal.com");
        setTo(customer.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/password/change").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }

}
