package gov.ao.usp.features.categoria.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.utils.http.ResponseHttp;
import ao.jcardoso.libs.utils.http.ResponseHttpBuilder;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaEditRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaRequest;
import gov.ao.usp.features.categoria.modelo.dto.CategoriaResponse;
import gov.ao.usp.features.categoria.service.CategoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usp/v1/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    
    private final CategoriaService service;

    @PostMapping
    public ResponseEntity<ResponseHttp<CategoriaResponse>> criar(
            @RequestBody @Valid CategoriaRequest req,
        HttpServletRequest request) {
        var response = service.criar(req);
        return ResponseHttpBuilder.ok(" Categoria criada com sucesso.", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttp<CategoriaResponse>> burcarPorID( @PathVariable UUID id) {
        var response = service.burcarPorID(id);
        return ResponseHttpBuilder.ok(" Categoria encontrada com sucesso.", response);
    }

    @PatchMapping
    public ResponseEntity<ResponseHttp<CategoriaResponse>> editar(@RequestBody @Valid CategoriaEditRequest req) {
        var response = service.editar(req);
        return ResponseHttpBuilder.ok("Categiria atualizada com sucesso.", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHttp<CategoriaResponse>> eliminar(@PathVariable UUID id) {
        var response = service.eliminar(id);
        return ResponseHttpBuilder.ok(" Categoria excluída com sucesso.", response);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ResponseHttp<PageResponseDTO<CategoriaResponse>>> listar(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction,
        @RequestParam(required = false) String descricao, @RequestParam(required = false) Boolean status) {

        PageRequestDTO dto = new PageRequestDTO();

        dto.setPage(page);
        dto.setSize(size);
        dto.setSortBy(sortBy);
        dto.setDirection(direction);

        var response = service.pesguisaEspecifica(dto, descricao, status);

        return ResponseHttpBuilder.ok("Lista carregada com sucesso.", response);
    }
}
