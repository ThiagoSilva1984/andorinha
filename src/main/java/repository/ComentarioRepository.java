package repository;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Comentario;
import model.exceptions.ErroAoConsultarBaseException;
import model.exceptions.ErroAoconectarNaBaseException;
import model.seletor.ComentarioSeletor;

@Stateless
public class ComentarioRepository extends AbstractCrudRepository {

	public void inserir(Comentario comentario) {
		comentario.setDataPostagem(Calendar.getInstance());
		super.em.persist(comentario);
	}

	public void atualizar(Comentario comentario) {
		comentario.setDataPostagem(Calendar.getInstance());
		super.em.merge(comentario);
	}

	public void remover(int id) {
		Comentario comentario = this.consultar(id);
		super.em.remove(comentario);
	}

	public Comentario consultar(int id) {
		return super.em.find(Comentario.class, id);
	}

	public List<Comentario> listarTodos() {
		ComentarioSeletor seletor = new ComentarioSeletor();
		List<Comentario> comentarios = this.pesquisar(seletor);

		return comentarios;
	}

	public List<Comentario> pesquisar(ComentarioSeletor seletor) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT c FROM Comentario c ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametros(query, seletor);

		return query.getResultList();
	}

	private void criarFiltro(StringBuilder jpql, ComentarioSeletor seletor) {

		if (seletor.possuiFiltro()) {
			jpql.append("WHERE ");
			boolean primeiro = true;

			// pesquisa por id do comentario
			if (seletor.getId() != null) {
				jpql.append("c.id = :id ");
				primeiro = false;
			}

			// pesquisa por uma palavra no comentário
			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("c.conteudo like :conteudo ");
				primeiro = false;
			}

			// pesquisa por comentários feitos em uma data especifica
			if (seletor.getDataPostagem() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}

				jpql.append("c.dataPostagem = :dataPostagem ");
				primeiro = false;
			}

			// pesquisa por comentários feitos em um periodo
			if (seletor.getDataPostagemInicial() != null && seletor.getDataPostagemFinal() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("c.dataPostagem BETWEEN :dataPostagemInicial AND :dataPostagemFinal ");
				primeiro = false;
			}

			// pesquisa por comentários feitos por um id usuário
			if (seletor.getIdUsuario() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}

				jpql.append("c.usuario.id = :idUsuario ");
				primeiro = false;
			}

			// pesqtisa comentarios pelo id tweet
			if (seletor.getIdTweet() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}

				jpql.append("c.tweet.id = :idTweet ");
				primeiro = false;
			}
		}
	}

	private void adicionarParametros(Query query, ComentarioSeletor seletor) {

		if (seletor.possuiFiltro()) {
			
			//Query do id comentário
			if (seletor.getId() != null) {
				query.setParameter("id", seletor.getId());
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				query.setParameter("conteudo", String.format("%%%s%%", seletor.getConteudo()));
			}

			if (seletor.getDataPostagemInicial() != null && seletor.getDataPostagemFinal() == null) {
				query.setParameter("dataPostagemInicial", seletor.getDataPostagemInicial());
			}

			if (seletor.getIdUsuario() != null) {
				query.setParameter("idUsuario", seletor.getIdUsuario());
			}

			if (seletor.getIdTweet() != null) {
				query.setParameter("idTweet", seletor.getIdTweet());
			}

			if (seletor.getDataPostagem() != null) {
				query.setParameter("dataPostagem", seletor.getDataPostagem());
			}

			if (seletor.getDataPostagemInicial() != null && seletor.getDataPostagemFinal() != null) {
				query.setParameter("dataPostagemInicial", seletor.getDataPostagemInicial());
				query.setParameter("dataPostagemFinal", seletor.getDataPostagemFinal());
			}
		}
	}

	public Long contar(ComentarioSeletor seletor) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(c) FROM Comentario c ");

		this.criarFiltro(jpql, seletor);

		Query query = super.em.createQuery(jpql.toString());

		this.adicionarParametros(query, seletor);

		return (Long) query.getSingleResult();
	}

}
