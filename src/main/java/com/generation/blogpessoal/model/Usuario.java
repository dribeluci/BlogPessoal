package com.generation.blogpessoal.model;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;



@Entity
@Table(name = "tb_usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Digite um Email válido")
	private String nome;

	@Schema(example = "email@email.com.br")
	@NotNull(message = "O campo Usuário é Obrigatório!")
	@Email(message = "O campo usuário deve ser um email válido!")
	private String usuario;

	@NotBlank(message = "O campo Senha é Obrigatório!")
	@Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres")
	private String senha;

	private String foto;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("usuario")
	private List<Postagem> postagem;
	
// criando um método construtor para ser utilizado no teste. eu não adicionei o postagem nesse
// construtor porque ele tem a função de listar as postagem e por estar associadas ao usuário, 
//	é um atributo preenchido automacamente pelo
//Relacionamento entre as Classes.

	public Usuario(Long id, String nome, String usuario, String senha, String foto) {
			
		super();
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
		this.senha = senha;
		this.foto = foto;
	}
	// criando outro construtor
	public Usuario() {
		
	}
	
	/* é através desses dois métodos construtor que o testes vai criar irá instanciar 
	 * alguns objetos da Classe Usuario nas nossas classes de teste.
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}
	}
	
	


