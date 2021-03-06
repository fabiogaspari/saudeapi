package br.com.saude.api.model.creation.builder.example;

import org.hibernate.sql.JoinType;
import org.javatuples.Triplet;

import br.com.saude.api.generic.CriteriaExample;
import br.com.saude.api.generic.GenericExampleBuilder;
import br.com.saude.api.generic.Helper;
import br.com.saude.api.model.entity.filter.AsoFilter;
import br.com.saude.api.model.entity.po.Aso;

public class AsoExampleBuilder extends GenericExampleBuilder<Aso, AsoFilter> {

	public static AsoExampleBuilder newInstance(AsoFilter filter) {
		return new AsoExampleBuilder(filter);
	}
	
	private AsoExampleBuilder(AsoFilter filter) {
		super(filter);
	}

	@Override
	protected void createExample() throws InstantiationException, IllegalAccessException {
		addStatus();
		addData();
		addValidade();
		addEmpregado();
		addAtendimento();
		addConforme();
		addNaoConformidades();
		addAusenciaExames();
		addImpressoSd2000();
		addPendente();
		addConvocado();
	}

	@Override
	protected void createExampleSelectList() throws InstantiationException, IllegalAccessException {
		
	}
	
	private void addStatus() {
		if(this.filter.getStatus() != null && this.filter.getStatus().length() > 0)
			this.entity.setStatus(this.filter.getStatus());
	}

	private void addData() {
		this.addData("data", this.filter.getData());
	}
	
	private void addValidade() {
		this.addData("validade", this.filter.getValidade());
	}
	
	private void addConforme() {
		this.entity.setConforme(this.addBoolean("conforme", this.filter.getConforme()));
	}
	
	private void addEmpregado() throws InstantiationException, IllegalAccessException {
		if(this.filter.getEmpregado()!=null) {
			CriteriaExample criteriaExample = EmpregadoExampleBuilder
					.newInstance(this.filter.getEmpregado()).getCriteriaExample();
			this.criterias.add(new Triplet<String,CriteriaExample,JoinType>("empregado", criteriaExample, JoinType.INNER_JOIN));
		}
	}
	
	private void addAtendimento() throws InstantiationException, IllegalAccessException {
		if(this.filter.getAtendimento()!=null) {
			CriteriaExample criteriaExample = AtendimentoExampleBuilder
					.newInstance(this.filter.getAtendimento()).getCriteriaExample();
			this.criterias.add(new Triplet<String,CriteriaExample,JoinType>("atendimento", criteriaExample, JoinType.INNER_JOIN));
		}
	}
	
	private void addNaoConformidades() {
		if(this.filter.getNaoConformidades() != null)
			this.entity.setNaoConformidades(Helper.filterLike(this.filter.getNaoConformidades()));
	}
	
	protected void addAusenciaExames() {
		this.entity.setAusenciaExames(this.addBoolean("ausenciaExames", this.filter.getAusenciaExames()));
	}
	protected void addImpressoSd2000() {
		this.entity.setImpressoSd2000(this.addBoolean("impressoSd2000", this.filter.getImpressoSd2000()));
	}
	protected void addPendente() {
		this.entity.setPendente(this.addBoolean("pendente", this.filter.getPendente()));
	}
	protected void addConvocado() {
		this.entity.setConvocado(this.addBoolean("convocado", this.filter.getConvocado()));
	}
}
