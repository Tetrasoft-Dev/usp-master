package gov.ao.usp.features.artigo.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ao.jcardoso.libs.paginacao.PageRequestDTO;
import ao.jcardoso.libs.paginacao.PageResponseDTO;
import ao.jcardoso.libs.utils.http.ResponseHttp;
import ao.jcardoso.libs.utils.http.ResponseHttpBuilder;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoAtualizarStockRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoEditRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoRequest;
import gov.ao.usp.features.artigo.modelo.dto.ArtigoResponse;
import gov.ao.usp.features.artigo.service.ArtigoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usp/v1/artigo")
@RequiredArgsConstructor
public class ArtigoController {

    private final ArtigoService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseHttp<ArtigoResponse>> criar(
            @RequestPart("artigo") @Valid ArtigoRequest req,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        
        var response = service.criar(req, imagem);
        return ResponseHttpBuilder.ok("Artigo criado com sucesso.", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseHttp<ArtigoResponse>> buscarPorId(@PathVariable UUID id) {
        var response = service.buscarPorId(id);
        return ResponseHttpBuilder.ok("Artigo encontrado com sucesso.", response);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseHttp<ArtigoResponse>> editar(
            @RequestPart("artigo") @Valid ArtigoEditRequest req,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        
        var response = service.editar(req, imagem);
        return ResponseHttpBuilder.ok("Artigo atualizado com sucesso.", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseHttp<ArtigoResponse>> eliminar(@PathVariable UUID id) {
        var response = service.eliminar(id);
        return ResponseHttpBuilder.ok("Artigo excluído com sucesso.", response);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<ResponseHttp<PageResponseDTO<ArtigoResponse>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataDeRegistro") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String nome, 
            @RequestParam(required = false) UUID id, // Removido o @PathVariable incorreto
            @RequestParam(required = false) Boolean status) {

        PageRequestDTO dto = new PageRequestDTO();
        dto.setPage(page);
        dto.setSize(size);
        dto.setSortBy(sortBy);
        dto.setDirection(direction);

        var response = service.pesquisaEspecifica(dto, nome, id, status);
        return ResponseHttpBuilder.ok("Lista carregada com sucesso.", response);
    }

   /*  @PutMapping("/{id}/stock")
    public ResponseEntity<ResponseHttp<ArtigoResponse>> atualizarQuantidade(
            @PathVariable UUID id,
            @RequestBody @Valid ArtigoAtualizarStockRequest req) {
        
        var response = service.atualizarQuantidade(id, req.getQuantidade(), req.getQuantidadeDanificada(), req.getEstado());
        return ResponseHttpBuilder.ok("Quantidade de stock atualizada com sucesso.", response);
    }*/
}