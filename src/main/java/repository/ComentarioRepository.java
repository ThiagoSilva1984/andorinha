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
		comentario.setData(Calendar.getInstance());
		super.em.persist(comentario);
	}

	public void atualizar(Comentario comentario) {
		comentario.setData(Calendar.getInstance());
		super.em.merge(comentario);
	}

	public void remover(int id) throws ErroAoConsultarBaseException, ErroAoconectarNaBaseException {
		Comentario comentario = this.consultar(id);
		super.em.remove(comentario);
	}

	public Comentario consultar(int id) throws ErroAoconectarNaBaseException, ErroAoConsultarBaseException {
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
			if (seletor.getData() != null && seletor.getData_final() == null) {
				if (!primeiro) {
					jpql.append("AND ");
				}

				jpql.append("c.data_postagem = :data ");
				primeiro = false;
			}

			// pesquisa por comentários feitos em um periodo
			if (seletor.getData() != null && seletor.getData_final() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}
				jpql.append("c.data_postagem BETWEEN :data AND :data_final ");
				primeiro = false;
			}

			// pesquisa por comentários feitos por um id usuário
			if (seletor.getIdUsuario() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}

				jpql.append("c.usuario.id = :id_usuario ");
				primeiro = false;
			}

			// pesqtisa comentarios pelo id tweet
			if (seletor.getIdTweet() != null) {
				if (!primeiro) {
					jpql.append("AND ");
				}

				jpql.append("c.tweet.id = :id_tweet ");
				primeiro = false;
			}
		}
	}

	private void adicionarParametros(Query query, ComentarioSeletor seletor) {

		if (seletor.possuiFiltro()) {

			if (seletor.getId() != null) {
				query.setParameter("id", seletor.getId());
			}

			if (seletor.getConteudo() != null && !seletor.getConteudo().trim().isEmpty()) {
				query.setParameter("conteudo", String.format("%%%s%%", seletor.getConteudo()));
			}

			if (seletor.getData() != null) {
				query.setParameter("data", seletor.getData());
			}

			if (seletor.getIdUsuario() != null) {
				query.setParameter("id_usuario", seletor.getIdUsuario());
			}

			if (seletor.getIdTweet() != null) {
				query.setParameter("id_tweet", seletor.getIdTweet());
			}

			if (seletor.getDataTweet() != null) {
				query.setParameter("data", seletor.getDataTweet());
			}

			if (seletor.getData() != null && seletor.getData_final() != null) {
				query.setParameter("data", seletor.getData());
				query.setParameter("data_final", seletor.getData_final());
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
