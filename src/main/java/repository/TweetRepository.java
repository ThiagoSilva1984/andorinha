package repository;

import java.util.List;

import javax.ejb.Stateless;

import model.Tweet;
import model.dto.TweetDTO;
import model.seletor.TweetSeletor;
import repository.base.AbstractCrudRepository;

@Stateless
public class TweetRepository extends AbstractCrudRepository<Tweet> {

	public List<Tweet> pesquisar(TweetSeletor seletor) {
		return super.createEntityQuery()
				.innerJoinFetch("usuario")
				.equal("id", seletor.getId())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("dataPostagem", seletor.getDataPostagemInicial())
				.between("dataPostagem", seletor.getDataPostagemInicial(), seletor.getDataPostagemFinal())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list();		
	}
	
	public List<TweetDTO> pesquisarDTO(TweetSeletor seletor) {
		return super.createTupleQuery()
				.select("id", "usuario.id as idUsuario", "conteudo", "usuario.nome as nomeUsuario", "dataPostagem")
				.join("usuario")
				.equal("id", seletor.getId())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("dataPostagem", seletor.getDataPostagemInicial())
				.between("dataPostagem", seletor.getDataPostagemInicial(), seletor.getDataPostagemFinal())
				.setFirstResult(seletor.getOffset())
				.setMaxResults(seletor.getLimite())
				.list(TweetDTO.class);	
	}
	
	public Long contar(TweetSeletor seletor) {
		return super.createCountQuery()
				//.innerJoinFetch("usuario")
				.equal("id", seletor.getId())
				.equal("usuario.id", seletor.getIdUsuario())
				.like("conteudo", seletor.getConteudo())
				.equal("dataPostagem", seletor.getDataPostagemInicial())
				.count();
	}
}
