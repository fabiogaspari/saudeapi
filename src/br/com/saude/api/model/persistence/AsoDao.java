package br.com.saude.api.model.persistence;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.saude.api.generic.GenericDao;
import br.com.saude.api.generic.GenericExampleBuilder;
import br.com.saude.api.generic.HibernateHelper;
import br.com.saude.api.generic.PagedList;
import br.com.saude.api.model.entity.po.Aso;
import br.com.saude.api.model.entity.po.Atendimento;
import br.com.saude.api.model.entity.po.Tarefa;

public class AsoDao extends GenericDao<Aso> {

	private static AsoDao instance;
	
	private AsoDao() {
		super();
	}
	
	public static AsoDao getInstance() {
		if(instance==null)
			instance = new AsoDao();
		return instance;
	}

	@Override
	protected void initializeFunctions() {
		this.functionLoadAll = aso -> {
			aso = this.functionLoad.apply(aso);
			
			if(aso.getAsoAlteracoes() != null)
				Hibernate.initialize(aso.getAsoAlteracoes());
			
			if(aso.getAsoAvaliacoes() != null)
				Hibernate.initialize(aso.getAsoAvaliacoes());
			
			if(aso.getAptidoes() != null)
				Hibernate.initialize(aso.getAptidoes());
			
			if(aso.getItemAuditoriaAsos() != null)
				Hibernate.initialize(aso.getItemAuditoriaAsos());
			
			aso = loadExamesConvocacao(aso);
			
			return aso;
		};
		
		this.functionLoad = aso -> {
			if(aso.getAtendimento() != null) {
				aso.setAtendimento((Atendimento)Hibernate.unproxy(aso.getAtendimento()));
				aso.getAtendimento().setTarefa((Tarefa) Hibernate.unproxy(aso.getAtendimento().getTarefa()));
				aso.getAtendimento().getFilaAtendimentoOcupacional().getAtualizacoes().size();
			}
			
			if(aso.getEmpregado() != null)
				Hibernate.initialize(aso.getEmpregado());
			
			return aso;
		};
	}
	
	public Aso getByIdLoadAll(Object id) throws Exception {
		return super.getById(id,this.functionLoadAll);
	}
	
	public PagedList<Aso> getListLoadAll(GenericExampleBuilder<?, ?> exampleBuilder) throws Exception {
		return super.getList(exampleBuilder,this.functionLoadAll);
	}
	
	@Override
	public PagedList<Aso> getList(GenericExampleBuilder<?, ?> exampleBuilder) throws Exception {
		return super.getList(exampleBuilder,this.functionLoad);
	}
	
	@SuppressWarnings("deprecation")
	public Aso getUltimoByEmpregado(Aso aso) {
		Session session = HibernateHelper.getSession();
		
		try {
			aso = (Aso) session.createCriteria(Aso.class)
					.createAlias("empregado", "empregado")
					.add(Restrictions.eq("empregado.id", aso.getEmpregado().getId()))
					.add(Restrictions.eq("conforme", true))
					.addOrder(Order.desc("validade"))
					.setFirstResult(0)
					.setMaxResults(1)
					.uniqueResult();
		}catch (Exception ex) {
			throw ex;
		}
		finally {
			HibernateHelper.close(session);
		}
		
		return aso;
	}
	
	private Aso loadExamesConvocacao(Aso aso) {
		if (aso.getExamesConvocacao() != null)
			Hibernate.initialize(aso.getExamesConvocacao());
		return aso;
	}
}
