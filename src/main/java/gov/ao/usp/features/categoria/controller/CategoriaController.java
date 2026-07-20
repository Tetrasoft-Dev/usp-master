package gov.ao.usp.features.categoria.controller;

import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ao.jcardoso.libs.utils.http.ResponseHttp;
import ao.jcardoso.libs.utils.http.ResponseHttpBuilder;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaResponse;
import gov.ao.usp.features.categoria.service.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    
    private CategoriaService service;

    @PostMapping
    public ResponseEntity<ResponseHttp<CategoriaResponse>> criar(
            @RequestBody @Valid CategoriaRequest req,
        HttpServletRequest request) {
        var response = service.criar(req);
        return ResponseHttpBuilder.ok(" Categoria criada com sucesso.", response);
    }
}
