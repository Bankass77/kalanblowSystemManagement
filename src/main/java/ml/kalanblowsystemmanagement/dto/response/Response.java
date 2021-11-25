package ml.kalanblowsystemmanagement.dto.response;




import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@JsonInclude(content = Include.NON_NULL)
@NoArgsConstructor
public class Response<T> {
    
	private Status status;
	private Object metadata;
	private Object errors;

	private T payload;

	public static <T> Response<T> badRequest() {
		Response<T> response = new Response<>();
		return response.setStatus(Status.BAD_REQUEST);

	}

	public static <T> Response<T> ok() {
		Response<T> response = new Response<>();
		return response.setStatus(Status.Ok);
	}

	public static <T> Response<T> unAuthorized() {
		Response<T> response = new Response<>();
		return response.setStatus(Status.UNAUTHORIZED);

	}

	public static <T> Response<T> validateException() {
		Response<T> response = new Response<>();
		return response.setStatus(Status.VALIDATION_EXCEPTION);
	}

	public static <T> Response<T> wrongCredentials() {
		Response<T> response = new Response<>();
		return response.setStatus(Status.WRONG_CREDENTIALS);

	}

	public static <T> Response<T> accessDenied() {
		Response<T> response = new Response<>();
		return response.setStatus(Status.ACCESS_DENIED);
	}

	public static <T> Response<T> exception() {
		Response<T> response = new Response<>();
		response.setStatus(Status.EXCEPTION);
		return response;

	}

	public static <T> Response<T> notFound() {

		Response<T> response = new Response<>();
		return response.setStatus(Status.NOT_FOUND);
	}

	public static <T> Response<T> duplicateEntity() {

		Response<T> response = new Response<>();
		return response.setStatus(Status.DUPLICATE_ENTITY);

	}

	public void addErrorMsgToResponse(String errorMsg, Exception ex) {

		ResponseError error = new ResponseError().setDetails(errorMsg).setMessage(ex.getMessage())
				.setLocalDateTime(LocalDateTime.now());
		setErrors(error);
	}

	@Getter
	@Setter
	@JsonInclude(content = Include.NON_NULL)
	@Accessors(chain = true)
	public static class PageMetadata {
		private final int size;
		private final long totalElements;
		private final int totalPages;
		private final int number;

		public PageMetadata(int size, long totalElements, int totalPages, int number) {
			super();
			this.size = size;
			this.totalElements = totalElements;
			this.totalPages = totalPages;
			this.number = number;
		}
	}
}
