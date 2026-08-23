package gov.ao.usp.features.departamento.service;

import java.util.ArrayList; // CORRIGIDO: Import necessário
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import gov.ao.usp.features.departamento.modelo.Departamento;
import gov.ao.usp.features.departamento.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupDataLoaderDepartamento implements CommandLineRunner {
    
    private final DepartamentoRepository departamentoRepository;

    @Override
    public void run(String... args) throws Exception {
        init();
    }

    public void init() {
        
        String[] initDepartamento = {"Finanças", "GOSP", "GPP", "AUDITORIA", "PESSOAL E QUADRO", "TX", "CIM", "6SEXTA", "POSTO COMANDO", "ARMA TEC", "GESTÃO DE INFRAESTRUTURA"};
          
        List<Departamento> entity = new ArrayList<>();
        if(departamentoRepository.findAll() != null){
           
            for (String registro : initDepartamento) {
              Departamento novoRegistro = new Departamento();
              novoRegistro.setPkDepartamento(UUID.randomUUID());
              novoRegistro.setAbreviacao(registro);
              novoRegistro.setDescricao(registro);
              novoRegistro.setStatus(Boolean.TRUE);
              entity.add(novoRegistro);
           }
        }
        

        departamentoRepository.saveAll(entity);
        log.info("Departamentos iniciais inseridos com sucesso!"); 
    }
}