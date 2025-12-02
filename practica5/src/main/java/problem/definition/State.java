package problem.definition;

import java.util.ArrayList;

import metaheuristics.generators.GeneratorType;

public class State implements Cloneable {
	
	protected GeneratorType typeGenerator;
	protected ArrayList<Double> evaluation;
	protected int number;
	protected ArrayList<Object> code;
	
	public State(State ps) {
		if (ps == null) {
			this.code = new ArrayList<Object>();
			this.evaluation = null;
			this.number = 0;
			this.typeGenerator = null;
			return;
		}
		typeGenerator = ps.getTypeGenerator();
		ArrayList<Double> eval = ps.getEvaluation();
		this.evaluation = (eval == null) ? null : new ArrayList<Double>(eval);
		number = ps.getNumber();
		ArrayList<Object> c = ps.getCode();
		this.code = (c == null) ? new ArrayList<Object>() : new ArrayList<Object>(c);
	}
	
	public State(ArrayList<Object> code) {
		super();
		this.code = (code == null) ? new ArrayList<Object>() : new ArrayList<Object>(code);
	}
	
	public State() {
		code=new ArrayList<Object>();
	}	
	
	public ArrayList<Object> getCode() {
		return (code == null) ? new ArrayList<Object>() : new ArrayList<Object>(code);
	}

	public void setCode(ArrayList<Object> listCode) {
		this.code = (listCode == null) ? new ArrayList<Object>() : new ArrayList<Object>(listCode);
	}

	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	
	public ArrayList<Double> getEvaluation() {
		return (evaluation == null) ? null : new ArrayList<Double>(evaluation);
	}

	public void setEvaluation(ArrayList<Double> evaluation) {
		this.evaluation = (evaluation == null) ? null : new ArrayList<Double>(evaluation);
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public State clone(){
		try {
			State s = (State) super.clone();
			// deep-copy mutable collections
			s.code = (this.code == null) ? new ArrayList<Object>() : new ArrayList<Object>(this.code);
			s.evaluation = (this.evaluation == null) ? null : new ArrayList<Double>(this.evaluation);
			// primitive and enum-like fields are safe to copy by assignment
			return s;
		} catch (CloneNotSupportedException e) {
			// fallback to copy constructor
			return new State(this);
		}
	}
	
	public Object getCopy(){
		return new State(this);
	}
	
	public boolean Comparator(State state){

		boolean result=false;
		if(state.getCode().equals(getCode())){
			result=true;
		}
		return result;
	}
	public double Distance(State state){
		double distancia = 0;
		for (int i = 0; i < state.getCode().size(); i++) {
			if (!(state.getCode().get(i).equals(this.getCode().get(i)))) {
				distancia++;
			}
		}
	return distancia;
	}
}
