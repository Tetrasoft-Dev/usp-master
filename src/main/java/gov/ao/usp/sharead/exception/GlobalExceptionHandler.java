package gov.ao.usp.sharead.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ao.jcardoso.libs.exception.BusinessException;
import ao.jcardoso.libs.exception.ResourceNotFoundException;
import ao.jcardoso.libs.utils.http.BusinessResponse;
import ao.jcardoso.libs.utils.http.ConflictResponse;
import ao.jcardoso.libs.utils.http.InternalServerResponse;
import ao.jcardoso.libs.utils.http.ResourceResponse;
import ao.jcardoso.libs.utils.http.ValidationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

     /**
         * RESOURCE NOT FOUND
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ResourceResponse> handleResourceNotFoundException(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {
                ResourceResponse response = new ResourceResponse();

                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setStatus(HttpStatus.NOT_FOUND.name());
                response.setMessage(ex.getMessage());
                log.warn("Recurso não encontrado: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(response);
        }

        /**
         * BUSINESS EXCEPTION
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<BusinessResponse> handleBusinessException(
                        BusinessException ex,
                        HttpServletRequest request) {
                BusinessResponse response = new BusinessResponse();

                response.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.name());
                response.setMessage(ex.getMessage());
                log.warn("Erro de regra de negócio: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                .body(response);
        }

        /**
         * VALIDAÇÃO DOS CAMPOS (@Valid)
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationResponse> handleValidationExceptions(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                Map<String, String> errors = new LinkedHashMap<>();

                for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                        errors.put(error.getField(), error.getDefaultMessage());
                }

                ValidationResponse response = new ValidationResponse();
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST.name());
                response.setMessage("Erro de validação nos dados enviados.");
                response.setErrors(errors);
                log.warn("Erro de validação: {}", response);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);

        }

        /**
         * QUALQUER ERRO NÃO TRATADO
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<InternalServerResponse> handleGlobalException(
                        Exception ex,
                        HttpServletRequest request) {

                log.error("Erro interno na URI {}: {}", request.getRequestURI(), ex.getMessage(), ex);

                InternalServerResponse response = new InternalServerResponse();

                response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
                response.setMessage("Erro interno do servidor.");
                response.setDetails("Ocorreu um erro inesperado ao processar a requisição.");

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ConflictResponse> handleConflictException(
                        ConflictException ex,
                        HttpServletRequest request) {

                ConflictResponse response = new ConflictResponse();

                response.setCode(HttpStatus.CONFLICT.value());
                response.setStatus(HttpStatus.CONFLICT.name());
                response.setMessage(ex.getMessage());

                log.warn("Conflito de dados: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(response);
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ResourceResponse> handleAuthorizationDeniedException(
                        AuthorizationDeniedException ex,
                        HttpServletRequest request) {

                log.warn("Acesso negado na URI {}: {}", request.getRequestURI(), ex.getMessage());

                ResourceResponse response = new ResourceResponse();

                response.setCode(HttpStatus.FORBIDDEN.value());
                response.setStatus(HttpStatus.FORBIDDEN.name());
                response.setMessage("Acesso negado.");
                response.setIdentifier("Você não possui permissão para acessar este recurso.");

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(response);
        }
}
