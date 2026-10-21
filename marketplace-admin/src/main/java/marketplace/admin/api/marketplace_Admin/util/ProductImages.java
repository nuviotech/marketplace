package marketplace.admin.api.marketplace_Admin.util;

public class ProductImages {
	
		
		String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		@Override
		public String toString() {
			return "{ \"url\":\"" + url
					+ "\" }";
		}
			
	
}
