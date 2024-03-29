package br.unitins.topicos1.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.NotFoundException;

import br.unitins.topicos1.dto.PessoaFisicaDTO;
import br.unitins.topicos1.dto.PessoaFisicaResponseDTO;
import br.unitins.topicos1.model.PessoaFisica;
import br.unitins.topicos1.repository.EstadoRepository;
import br.unitins.topicos1.repository.PessoaFisicaRepository;

@ApplicationScoped
public class PessoaFisicaServiceImpl implements PessoaFisicaService {

    @Inject
    PessoaFisicaRepository pessoaFisicaRepository;

    @Inject
    Validator validator;

    @Override
    public List<PessoaFisicaResponseDTO> getAll() {
        List<PessoaFisica> list = pessoaFisicaRepository.listAll();
        return list.stream().map(PessoaFisicaResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public PessoaFisicaResponseDTO findById(Long id) {
        PessoaFisica pessoafisica = pessoaFisicaRepository.findById(id);
        if (pessoafisica == null)
            throw new NotFoundException("Pessoa Física não encontrada.");
        return new PessoaFisicaResponseDTO(pessoafisica);
    }

    @Override
    @Transactional
    public PessoaFisicaResponseDTO create(PessoaFisicaDTO pessoaFisicaDTO) throws ConstraintViolationException {
        validar(pessoaFisicaDTO);

        PessoaFisica entity = new PessoaFisica();
        entity.setCpf(pessoaFisicaDTO.getCpf());
        entity.setNome(pessoaFisicaDTO.getNome());

        pessoaFisicaRepository.persist(entity);

        return new PessoaFisicaResponseDTO(entity);
    }

    @Override
    @Transactional
    public PessoaFisicaResponseDTO update(Long id, PessoaFisicaDTO pessoaFisicaDTO) throws ConstraintViolationException{
        validar(pessoaFisicaDTO);
   
        PessoaFisica entity = pessoaFisicaRepository.findById(id);
        entity.setCpf(pessoaFisicaDTO.getCpf());
        entity.setNome(pessoaFisicaDTO.getNome());

        return new PessoaFisicaResponseDTO(entity);
    }

    private void validar(PessoaFisicaDTO pessoaFisicaDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<PessoaFisicaDTO>> violations = validator.validate(pessoaFisicaDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);


    }

    @Override
    @Transactional
    public void delete(Long id) {
        pessoaFisicaRepository.deleteById(id);
    }

    @Override
    public List<PessoaFisicaResponseDTO> findByNome(String nome) {
        List<PessoaFisica> list = pessoaFisicaRepository.findByNome(nome);
        return list.stream().map(PessoaFisicaResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return pessoaFisicaRepository.count();
    }

}
