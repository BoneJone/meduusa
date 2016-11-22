package me.nicou.tuntikirjaus.bean;

public class ViestiImpl  implements Viesti {
	
		String otsikko;
		String viesti;
		String tyyppi;
		
		public ViestiImpl() {
			super();
			
		}

		public ViestiImpl(String otsikko, String viesti, String tyyppi) {
			super();
			this.otsikko = otsikko;
			this.viesti = viesti;
			this.tyyppi = tyyppi;
		}

		public String getOtsikko() {
			return otsikko;
		}

		public void setOtsikko(String otsikko) {
			this.otsikko = otsikko;
		}

		public String getViesti() {
			return viesti;
		}

		public void setViesti(String viesti) {
			this.viesti = viesti;
		}

		public String getTyyppi() {
			return tyyppi;
		}

		public void setTyyppi(String tyyppi) {
			this.tyyppi = tyyppi;
		}

		@Override
		public String toString() {
			return "ViestiImpl [otsikko=" + otsikko + ", viesti=" + viesti
					+ ", tyyppi=" + tyyppi + "]";
		}
		
}
