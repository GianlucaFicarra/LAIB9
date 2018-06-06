package it.polito.tdp.borders.model;

public class Country implements Comparable<Country> {

	private int cCode; 			// Country codice id
	private String statoAbr; 	// un’abbreviazione	univoca di 3 lettere
	private String statoNome; 	// nome completo

	
	public Country(int cCode, String statoAbr, String statoNome) {
		this.cCode = cCode;
		this.statoAbr = statoAbr;
		this.statoNome = statoNome;
	}

	public Country(int cCode) {
		this.cCode = cCode;
	}

	public int getcCode() {
		return cCode;
	}

	public void setcCode(int cCode) {
		this.cCode = cCode;
	}

	public String getStatoAbr() {
		return statoAbr;
	}

	public void setStatoAbr(String statoAbr) {
		this.statoAbr = statoAbr;
	}

	public String getStatoNome() {
		return statoNome;
	}

	public void setStatoNome(String statoNome) {
		this.statoNome = statoNome;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (cCode != other.cCode)
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return String.format("%s", statoNome);
	}

	/*@Override quella pura creata da lui è questa
	public String toString() {
		return "Country [statoAbr=" + statoAbr + "]";
	}*/

	@Override 
	public int compareTo(Country o) { //per darmi gli stati in ordine alfabetico
		return this.getStatoNome().compareTo(o.getStatoNome());
	}

}
