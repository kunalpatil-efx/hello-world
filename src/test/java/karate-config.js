function fn() {
	var env = karate.env
	
	if(!env) {
		env = 'local';
	}
	var config = {
			
	}
	if(env == 'local') {
		config.COUNTRIES_BASE = 'https://restcountries.eu';
	}
	return config;
}