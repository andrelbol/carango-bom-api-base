package br.com.caelum.carangobom.veiculo.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import br.com.caelum.carangobom.veiculo.controller.dto.VeiculoDto;
import br.com.caelum.carangobom.veiculo.controller.form.VeiculoForm;
import br.com.caelum.carangobom.veiculo.model.Veiculo;
import br.com.caelum.carangobom.veiculo.repository.VeiculoRepository;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

	private VeiculoRepository veiculoRepository;

	private MarcaRepository marcaRepository;

	@Autowired
	public VeiculoController(VeiculoRepository veiculoRepository, MarcaRepository marcaRepository) {
		this.veiculoRepository = veiculoRepository;
		this.marcaRepository = marcaRepository;
	}

	@GetMapping
	public List<VeiculoDto> listar() {
		return VeiculoDto.converter(veiculoRepository.findByOrderByModelo());
	}

	@GetMapping("/{id}")
	public ResponseEntity<VeiculoDto> listarPorId(@PathVariable Long id) {
		Optional<Veiculo> veiculoEncontrada = veiculoRepository.findById(id);

		return veiculoEncontrada
				.map(veiculo -> ResponseEntity.ok(VeiculoDto.parse(veiculo)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	@Transactional
	public ResponseEntity<VeiculoDto> cadastrar(
			@Valid @RequestBody VeiculoForm veiculoForm,
			UriComponentsBuilder uriBuilder) {
		Optional<Marca> marca = marcaRepository.findById(veiculoForm.getMarcaId());

		if (marca.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Veiculo veiculoCadastrado = veiculoRepository.save(veiculoForm.converter(marca.get()));
		URI h = uriBuilder.path("/veiculos/{id}").buildAndExpand(veiculoCadastrado.getId()).toUri();

		return ResponseEntity.created(h).body(VeiculoDto.parse(veiculoCadastrado));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<VeiculoDto> alterar(@PathVariable Long id, @Valid @RequestBody VeiculoForm veiculoForm) {
		Optional<Marca> marca = marcaRepository.findById(veiculoForm.getMarcaId());

		if (marca.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<Veiculo> veiculoAtual = veiculoRepository.findById(id);
		if (veiculoAtual.isPresent()) {
			Veiculo veiculoAlterada = veiculoForm.atualizar(veiculoAtual.get(), marca.get());
			return ResponseEntity.ok(VeiculoDto.parse(veiculoAlterada));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity deletar(@PathVariable Long id) {
		Optional<Veiculo> veiculo = veiculoRepository.findById(id);
		if (veiculo.isPresent()) {
			veiculoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}
}
