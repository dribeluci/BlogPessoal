package com.generation.blogpessoal.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name = "tb_tema")
	public class Tema {
		
		@Id
		@GeneratedValue (strategy = GenerationType.IDENTITY)
		private long id;
		
		@NotNull
		private String descricao;
		
		@OneToMany(mappedBy = "tema", cascade = CascadeType.REMOVE)//esse comando serve para referenciar a chave e também toda vez que remover um tema, será apagada todas
		// as postagens relacionadas a esse determinado tema.
		@JsonIgnoreProperties("tema") // essa anotação serve para evitar travamentos no código e consequentemente evita erros.
		private List<Postagem>postagem; // aqui eu crio um objeto do tipo lista, que referencia a 
		// relação 1:N, pois um tema pode ter várias postagens, por isso foi feito uma lista de postagem.
		
	

		public long getId() {
			return id;
		}

		public List<Postagem> getPostagem() {
			return postagem;
		}

		public void setPostagem(List<Postagem> postagem) {
			this.postagem = postagem;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		

	}


