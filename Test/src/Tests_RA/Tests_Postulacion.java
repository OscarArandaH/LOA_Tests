package Tests_RA;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests_Postulacion extends Vars_RA{
  @Test
	public void Test_1_Postulaciones_Inscribir_Random() throws InterruptedException {
		System.out.println("Se inicia el test Test_1_Postulaciones_Inscribir_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Vars_RA.driverPath);
		System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY, Vars_RA.logPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(Vars_RA.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(Vars_RA.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("rutest")).click();
		driver.findElement(By.id("rutest")).sendKeys(Vars_RA.userRUTAlt);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(Vars_RA.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		TimeUnit.MILLISECONDS.sleep(350);
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de postulacion");
			System.out.println("Se finaliza el test Test_1_Postulaciones_Inscribir_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		// Se revisa si el proceso esta abierto
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try {
			driver.switchTo().frame("derecho");
		} catch (Exception e2) {
			System.out.println("El proceso se encuentra cerrado");
			System.out.println("Se finaliza el test Test_1_Postulaciones_Inscribir_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame("derecho");
		// Se obtienen todas las asignaturas
		List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector("tr"));
		TimeUnit.MILLISECONDS.sleep(350);
		// Se almacenan todas las asignaturas
		ArrayList<String> listadoAsignaturasAux = new ArrayList<String>();
		for (WebElement e : listadoAsignaturas) {
			// Se ignora el encabezado de la lista
			if( e.getText().split(" ")[2].compareTo("N") != 0  ){
				String[] asignatura = e.getText().split(" ");
				String tmp = "";
				for( int i = 1; i < asignatura.length - 1 ; i++ ) {
					tmp += " " + asignatura[i];
				}
				listadoAsignaturasAux.add(tmp.strip());
			}
		}
		// Se revisan todos los cursos
		int i = 0;
		while( i == 0) {
			// Seleccionar frame del listado de cursos
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("derecho");
			Boolean existeTeoria = false;
			Boolean existeLaboratorio = false;
			Boolean existeEjercicio = false;
			Boolean seleccionoTeoria = false;
			Boolean seleccionoLaboratorio = false;
			Boolean seleccionoEjercicio = false;
			// Se revisa que existan asignaturas
			if( listadoAsignaturasAux.size() == 0 ) {
				System.out.println("No hay mas asignaturas");
				i = 1;
			} else {
				// Se crea el random para seleccionar una asignatura al azar
				Random rand = new Random();
				int rand_int1 = rand.nextInt((listadoAsignaturasAux.size() - 1) + 1) + 1;
				// Se busca una asignatura
				WebElement asignatura = driver.findElement(By.linkText(listadoAsignaturasAux.get(rand_int1-1)));
				// Se crea el JavascriptExecutor para hacer scroll
				JavascriptExecutor js = (JavascriptExecutor) driver;
				// Se obtiene la posicion del boton
				Point location = asignatura.getLocation();
				// Se hace scroll hacia el boton
				js.executeScript("window.scrollBy(0,"+location.getY()+")");
				// Se selecciona una asignatura
				asignatura.click();
				System.out.println("\n	Asignatura: " + listadoAsignaturasAux.get(rand_int1-1));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se sale del frame, y se entra al frame de los cursos de teoria.
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_cteo");
				// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
				driver.switchTo().frame("mainFrame");
				System.out.println("	Se revisan los cursos de teoria");
				// Se obtienen todos los cursos
				List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se almacenan todas las secciones
				ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
				for (WebElement e : listadoTeoria) {
					String tmp = e.getText().replace("\n", " ");
					if( tmp.equals(Vars_RA.sinCoordinacionInscribirTeoriaText) ){
						// No hay cursos de teoria
						System.out.println("		No hay cursos de teoria");
					} else {
						// Existen coordinaciones
						existeTeoria = true;
						// Se crea un random para seleccionar una seccion de teoria al azar
						int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
						// Se obtiene la seccion
						WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)"));
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
						// Se selecciona la seccion
						seccionElement.click();
						seleccionoTeoria = true;
						System.out.println("			Si hay cursos de teoria");
						listadoTeoriaAux.add(tmp);
					}
				}
				// Se sale del frame, y se entra al frame de los cursos de laboratorio.
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_clab");
				// Se selecciona el frame de los cursos (::LISTADO LABORATORIOS es un frame)
				driver.switchTo().frame("mainFrame");
				System.out.println("	Se revisan los cursos de laboratorio");
				// Se obtienen todos los cursos
				List<WebElement> listadoLaboratorio = driver.findElements(By.cssSelector("tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se almacenan todas las secciones
				ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
				for (WebElement e : listadoLaboratorio) {
					String tmp = e.getText().replace("\n", " ");
					if( tmp.equals(Vars_RA.sinCoordinacionInscribirLabText) ){
						// No hay cursos de laboratorio
						System.out.println("		No hay cursos de laboratorio");
					} else {
						listadoLaboratorioAux.add(tmp);
						existeLaboratorio = true;
					}
				}
				// Se prueba seleccionar una seccion de laboratorio
				for ( int j = 0; j < listadoLaboratorioAux.size(); j++ ) {
					String seccion = listadoLaboratorioAux.get(j).replace("\n", " "	).strip();
					String[] seccionAux = seccion.split(" ");
					int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
					System.out.println("		Seccion " + (j+1) + " | " + seccion);
					System.out.println("		Seccion " + (j+1) + " | Cupos: " + cupos);
					// Se revisa si existe cupo
					if( cupos > 0 ){
						// Si hay cupos, se hace scroll y luego se selecciona
						WebElement seccionElement = driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(j+1)+") > td:nth-child(1)"));
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
						// Se selecciona la seccion
						seccionElement.click();
						seleccionoLaboratorio = true;
						System.out.println("		Seccion " + (j+1) + " | Seleccionada");
						break;
					} else {
						// Si no hay cupos, se selecciona el siguiente curso
						System.out.println("		Seccion " + (j+1) + " | Sin cupos");
					}
				}
				// Se sale del frame, y se entra al frame de los cursos de ejercicio.
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_ceje");
				// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
				driver.switchTo().frame("mainFrame");
				System.out.println("	Se revisan los cursos de ejercicio");
				// Se obtienen todos los cursos
				List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se almacenan todas las secciones
				ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
				for (WebElement e : listadoEjercicio) {
					String tmp = e.getText().replace("\n", " ");
					if( tmp.equals(Vars_RA.sinCoordinacionInscribirEjeText) ){
						// No hay cursos de ejercicios
						System.out.println("		No hay cursos de ejercicios");
					} else {
						// Existen coordinaciones
						existeEjercicio = true;
						// Se crea un random para seleccionar una seccion de teoria al azar
						int rand_int2 = rand.nextInt((listadoEjercicio.size() - 1) + 1) + 1;
						// Se obtiene la seccion
						WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
						// Se selecciona la seccion
						seccionElement.click();
						seleccionoEjercicio = true;
						System.out.println("			Si hay cursos de teoria");
						listadoEjercicioAux.add(tmp);
					}
				}
				// Se revisa que exista al menos un curso
				if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
					// Se puede postular a la asignatura
					System.out.println("	Se puede postular a la asignatura");
					// Se sale del frame, y se entra al frame deonde esta el boton de postular
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame(5);
					// Se postula a la asignatura
					driver.findElement(By.id("btn_postular")).click();
					TimeUnit.MILLISECONDS.sleep(350);
					if (driver.switchTo().alert().getText().compareTo(Vars_RA.alertaInscribirText) == 0 ) {
						driver.switchTo().alert().accept();
					}
					// Tiempo para guardar la asignatura postulada
					TimeUnit.MILLISECONDS.sleep(350);
					i = 1;
				} else {
					// No se puede postular a la asignatura
					System.out.println("	No se puede postular a la asignatura");
				}
			}
		}
		System.out.println("Se finaliza el test Test_1_Postulaciones_Inscribir_Random\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	public void Test_2_Postulaciones_Desinscribir_Random() throws InterruptedException {
		System.out.println("Se inicia el test Test_2_Postulaciones_Desinscribir_Random");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Vars_RA.driverPath);
		System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY, Vars_RA.logPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(Vars_RA.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(Vars_RA.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("rutest")).click();
		driver.findElement(By.id("rutest")).sendKeys(Vars_RA.userRUTAlt);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(Vars_RA.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		TimeUnit.MILLISECONDS.sleep(350);
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de postulacion");
			System.out.println("Se finaliza el test Test_2_Postulaciones_Desinscribir_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		// Se revisa si el proceso esta abierto
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try {
			driver.switchTo().frame("derecho");
		} catch (Exception e2) {
			System.out.println("El proceso se encuentra cerrado");
			System.out.println("Se finaliza el test Test_2_Postulaciones_Desinscribir_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Se entra al frame con las asignaturas postuladas
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se buscan la cantidad de asignaturas postuladas
		List<WebElement> accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		TimeUnit.MILLISECONDS.sleep(350);
		List<WebElement> codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		TimeUnit.MILLISECONDS.sleep(350);
		int postulacionesNoDesinscribibles = 0;
		for( int i = 0; i < accionesAsignaturasPostuladas.size() ; i++ ){
			if( !accionesAsignaturasPostuladas.get(i).getText().equals(Vars_RA.btnPostulacionDESINSCRIBIRText) ){
				postulacionesNoDesinscribibles++;
			}
		}
		if( postulacionesNoDesinscribibles == accionesAsignaturasPostuladas.size() ){
			System.out.println("No hay asignaturas para desinscribir");
		} else {
			// Se crea un arraylist para guardar los randoms ya utilizados (para no repetirlos
			ArrayList<Integer> randomOmitidos = new ArrayList<Integer>();
			int i = 0;
			while( i == 0){
				int cantidadBotones = accionesAsignaturasPostuladas.size();
				// Se crea el random para seleccionar una asignatura al azar
				Random rand = new Random();
				int rand_int = rand.nextInt((cantidadBotones - 1) + 1) + 1;
				while( (randomOmitidos.contains(rand_int)) && (randomOmitidos.size() < postulacionesNoDesinscribibles ) ){
					rand_int = rand.nextInt((cantidadBotones - 1) + 1) + 1;
				}
				int j = 0;
				while( j < cantidadBotones ){
					if( accionesAsignaturasPostuladas.get(j).getText().equals(Vars_RA.btnPostulacionDESINSCRIBIRText) ){
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = accionesAsignaturasPostuladas.get(j).getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						String codigo = codigosAsignaturasPostuladas.get(j).getText().split("-")[0].strip();
						// Se selecciona la solicitud
						accionesAsignaturasPostuladas.get(j).click();
						// Se acepta el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Tiempo para esperar que se abra la alerta
						TimeUnit.MILLISECONDS.sleep(350);
						// Se ingresa el codigo de desinscripcion
						driver.switchTo().alert().sendKeys(codigo);
						// Se acepta el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Se entra al frame con las asignaturas postuladas
						driver.switchTo().defaultContent();
						// Tiempo para esperar que se abra la alerta
						TimeUnit.MILLISECONDS.sleep(350);
						// Se entra al frame con las asignaturas postuladas
						driver.switchTo().defaultContent();
						driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
						try {
							driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
						} catch (Exception e) {
							System.out.println("No se encuentra el proceso de postulacion");
							System.out.println("Se finaliza el test Test_2_Postulaciones_Desinscribir_Random\n\n==========================================================\n");
							driver.quit();
							return;
						}
						try {
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("derecho");
							driver.switchTo().defaultContent();
						} catch (Exception e2) {
							System.out.println("El proceso se encuentra cerrado");
							System.out.println("Se finaliza el test Test_2_Postulaciones_Desinscribir_Random\n\n==========================================================\n");
							driver.quit();
							return;
						}
						TimeUnit.MILLISECONDS.sleep(350);
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame(5);
						// Se buscan la cantidad de asignaturas restantes
						accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
						TimeUnit.MILLISECONDS.sleep(350);
						codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
						TimeUnit.MILLISECONDS.sleep(350);
						cantidadBotones = accionesAsignaturasPostuladas.size();
						j = cantidadBotones;
						i = 1;
					} else {
						j++;
					}
				}
			}
		}
		System.out.println("Se finaliza el test Test_2_Postulaciones_Desinscribir_Random\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	public void Test_3_Postulaciones_Inscribir_Limite() throws InterruptedException {
		System.out.println("Se inicia el test Test_3_Postulaciones_Inscribir_Limite");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Vars_RA.driverPath);
		System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY, Vars_RA.logPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(Vars_RA.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(Vars_RA.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("rutest")).click();
		driver.findElement(By.id("rutest")).sendKeys(Vars_RA.userRUTAlt);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(Vars_RA.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		TimeUnit.MILLISECONDS.sleep(350);

		int CANTIDADASIGNATURASLIMITE = 6;

		// Se busca cuantas asignaturas lleva solicitadas y postuladas
		int contadorAsignaturas = 0;
		Boolean procesoSolicitudes = true;
		Boolean procesoInscripciones = true;
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de postulacion");
			System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		// Se revisa si el proceso esta abierto
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try {
			driver.switchTo().frame("derecho");
		} catch (Exception e2) {
			System.out.println("El proceso se encuentra cerrado");
			System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar proceso solicitudes
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de solicitudes");
			procesoSolicitudes = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar proceso inscripciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.inscripcionText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de inscripcion");
			procesoInscripciones = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(350);
		// Si esta abierto el proceso de postulaciones, se cuentan cuantas postulaciones hay
		if ( procesoSolicitudes ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
			TimeUnit.MILLISECONDS.sleep(350);
			// Seleccionar el frame con las postulaciones
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame(5);
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasPostuladas = driver.findElements(By.className("nivel"));
			TimeUnit.MILLISECONDS.sleep(350);
			// Se almacenan todas las asignaturas postuladas
			ArrayList<String> listadoAsignaturasPostuladasAux = new ArrayList<String>();
			for (WebElement linea : listadoAsignaturasPostuladas) {
				listadoAsignaturasPostuladasAux.add(linea.getText().replace("\n", " ").strip());
			}
			contadorAsignaturas += listadoAsignaturasPostuladasAux.size();
		}
		TimeUnit.MILLISECONDS.sleep(350);
		// Si esta abierto el proceso de inscripciones, se cuentan cuantas inscripciones hay
		if ( procesoInscripciones ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
			driver.findElement(By.linkText(Vars_RA.inscripcionText)).click();
			TimeUnit.MILLISECONDS.sleep(350);
			// Seleccionar el frame con las inscripciones
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_inscripcion");
			TimeUnit.MILLISECONDS.sleep(350);
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasInscritas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
			// Se cuenta cuantas asignaturas se han inscrito
			ArrayList<String> codigosInscripciones = new ArrayList<String>();
			int i = 0;
			while ( i < listadoAsignaturasInscritas.size() ){
				String tmp = listadoAsignaturasInscritas.get(i).getText().split("-")[0].trim();
				if( !codigosInscripciones.contains(tmp) ){
					codigosInscripciones.add(tmp);
				}
				i++;
			}
			contadorAsignaturas += codigosInscripciones.size();
		}
		TimeUnit.MILLISECONDS.sleep(350);
		int i = 0;
		while ( i == 0 ){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			for( int j = 1; j <= 3; j++ ){
				System.out.println("Vuelta " + j );
				// Se revisa si se puede seleccionar el proceso de postulaciones
				driver.switchTo().defaultContent();
				driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
				try {
					driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
				} catch (Exception e) {
					System.out.println("No se encuentra el proceso de postulacion");
					System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				// Se revisa si el proceso esta abierto
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				try {
					driver.switchTo().frame("derecho");
				} catch (Exception e2) {
					System.out.println("El proceso se encuentra cerrado");
					System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				driver.switchTo().defaultContent();
				TimeUnit.MILLISECONDS.sleep(350);
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				contadorAsignaturas += listadoAsignaturas2.size();
				ArrayList<ArrayList<String>> listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
				for (WebElement linea : listadoAsignaturas2) {
					String codigo = linea.getText().split(" ")[0];
					String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
					listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}
				// Se crea una lista para almacenar las asignaturas que no se pudieron inscribir
				ArrayList<Integer> codigosAsignaturasAOmitir2 = new ArrayList<Integer>();
				// Se revisan todos los cursos
				int l = 0;
				while( (l == 0) && (contadorAsignaturas < CANTIDADASIGNATURASLIMITE) ) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					listadoAsignaturas2 = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
					TimeUnit.MILLISECONDS.sleep(350);
					listadoCodigoNombre2 = new ArrayList<ArrayList<String>>();
					for (WebElement linea : listadoAsignaturas2) {
						String codigo = linea.getText().split(" ")[0];
						String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
						listadoCodigoNombre2.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
					}
					if( listadoCodigoNombre2.size() == 0 ){
						System.out.println("	No hay asignaturas disponibles para postular");
						l = 1;
					} else {
						Boolean existeTeoria = false;
						Boolean existeLaboratorio = false;
						Boolean existeEjercicio = false;
						Boolean seleccionoTeoria = false;
						Boolean seleccionoLaboratorio = false;
						Boolean seleccionoEjercicio = false;
						// Se crea el random para seleccionar una asignatura al azar
						Random rand = new Random();
						int rand_int1 = rand.nextInt((listadoCodigoNombre2.size() - 1) + 1) + 1;
						// Se revisa cuantas asignaturas se omitieron
						int asignaturasOmitidas = 0;
						// Mientras el while este en la lista de omitidos y no se alcance el limite, se busca otro
						while( (codigosAsignaturasAOmitir2.contains(Integer.parseInt(listadoCodigoNombre2.get(rand_int1-1).get(0)))) && (asignaturasOmitidas <= listadoCodigoNombre2.size() ) ){
							rand_int1 = rand.nextInt((listadoCodigoNombre2.size() - 1) + 1) + 1;
							asignaturasOmitidas++;
						}
						// Si se revisaron todas las asignaturas en una vuelta, sale de la vuelta
						if( asignaturasOmitidas > listadoCodigoNombre2.size() ){
							l = 1;
						} else {
							// Se revisa si se puede seleccionar el proceso de postulaciones
							driver.switchTo().defaultContent();
							driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
							try {
								driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
							} catch (Exception e) {
								System.out.println("No se encuentra el proceso de postulacion");
								System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							// Se revisa si el proceso esta abierto
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							try {
								driver.switchTo().frame("derecho");
							} catch (Exception e2) {
								System.out.println("El proceso se encuentra cerrado");
								System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
								driver.quit();
								return;
							}
							driver.switchTo().defaultContent();
							TimeUnit.MILLISECONDS.sleep(350);
							// Se entra al frame de los derechos
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("derecho");
							// Se busca una asignatura
							WebElement asignatura = driver.findElement(By.linkText(listadoCodigoNombre2.get(rand_int1).get(1)));
							// Se crea el JavascriptExecutor para hacer scroll
							JavascriptExecutor js = (JavascriptExecutor) driver;
							// Se obtiene la posicion del boton
							Point location = asignatura.getLocation();
							// Se hace scroll hacia el boton
							js.executeScript("window.scrollBy(0,"+location.getY()+")");
							// Se hace click en la asignatura
							asignatura.click();
							System.out.println("\n	Asignatura: " + listadoCodigoNombre2.get(rand_int1).get(0) + " - " +  listadoCodigoNombre2.get(rand_int1).get(1) );
							TimeUnit.MILLISECONDS.sleep(350);
							// Se sale del frame, y se entra al frame de los cursos de teoria.
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_cteo");
							// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
							driver.switchTo().frame("mainFrame");
							System.out.println("		Se revisan los cursos de teoria");
							// Se obtienen todos los cursos
							List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
							TimeUnit.MILLISECONDS.sleep(350);
							// Se obtiene la primera linea despues de buscar tr
							String primeraLinea = listadoTeoria.get(0).getText().strip();
							// Si la primera linea dice que no hay coordinaciones, se avisa
							if( !primeraLinea.equals(Vars_RA.sinCoordinacionInscribirTeoriaText) ){
								// Existen coordinaciones
								existeTeoria = true;
								// Se crea un random para seleccionar una seccion de teoria al azar
								int rand_int2 = rand.nextInt((listadoTeoria.size() - 1) + 1) + 1;
								// Se obtiene la seccion
								WebElement seccionElement = driver.findElement(By.cssSelector(".row-curso:nth-child("+(rand_int2)+") > td:nth-child(1)"));
								// Se hace scroll hacia el boton
								js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
								// Se selecciona la seccion
								seccionElement.click();
								seleccionoTeoria = true;
								System.out.println("			Si hay cursos de teoria");
							} else {
								// No hay cursos de teoria
								System.out.println("			No hay cursos de teoria");
							}
							// Se sale del frame, y se entra al frame de los cursos de laboratorio.
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_clab");
							// Se selecciona el frame de los cursos (::LISTADO LABORATORIOS es un frame)
							driver.switchTo().frame("mainFrame");
							System.out.println("		Se revisan los cursos de laboratorio");
							// Se obtienen todos los cursos
							List<WebElement> listadoLaboratorio = driver.findElements(By.cssSelector("tr"));
							TimeUnit.MILLISECONDS.sleep(350);
							// Se almacenan todas las secciones
							ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
							for (WebElement e : listadoLaboratorio) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_RA.sinCoordinacionInscribirLabText) ){
									// No hay cursos de laboratorio
									System.out.println("			No hay cursos de laboratorio");
								} else {
									listadoLaboratorioAux.add(tmp);
									existeLaboratorio = true;
								}
							}
							// Se prueba seleccionar una seccion de laboratorio
							for ( int k = 0; k < listadoLaboratorioAux.size(); k++ ) {
								String seccion = listadoLaboratorioAux.get(k).replace("\n", " "	).strip();
								String[] seccionAux = seccion.split(" ");
								int cupos = Integer.parseInt(seccionAux[seccionAux.length - 1]);
								System.out.println("			Seccion " + (k+1) + " | " + seccion);
								System.out.println("			Seccion " + (k+1) + " | Cupos: " + cupos);
								// Se revisa si existe cupo
								if( cupos > 0 ){
									// Si hay cupos, se hace scroll y luego se selecciona
									WebElement seccionElement = driver.findElement(By.cssSelector("tr.row-laboratorio:nth-child("+(k+1)+") > td:nth-child(1) > div:nth-child(1) > input:nth-child(1)"));
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
									// Se selecciona la seccion
									seccionElement.click();
									seleccionoLaboratorio = true;
									System.out.println("			Seccion " + (k+1) + " | Seleccionada");
									break;
								} else {
									// Si no hay cupos, se selecciona el siguiente curso
									System.out.println("			Seccion " + (k+1) + " | Sin cupos");
								}
							}
							// Se sale del frame, y se entra al frame de los cursos de ejercicio.
							driver.switchTo().defaultContent();
							driver.switchTo().frame("mainFrame");
							driver.switchTo().frame("frame_ceje");
							// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
							driver.switchTo().frame("mainFrame");
							System.out.println("		Se revisan los cursos de ejercicio");
							// Se obtienen todos los cursos
							List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
							TimeUnit.MILLISECONDS.sleep(350);
							// Se almacenan todas las secciones
							ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
							for (WebElement e : listadoEjercicio) {
								String tmp = e.getText().replace("\n", " ");
								if( tmp.equals(Vars_RA.sinCoordinacionInscribirEjeText) ){
									// No hay cursos de ejercicios
									System.out.println("			No hay cursos de ejercicios");
								} else {
									listadoEjercicioAux.add(tmp);
									existeEjercicio = true;
									// Se crea un random para seleccionar una seccion de ejercicio al azar
									int rand_int2 = rand.nextInt((listadoEjercicio.size() - 1) + 1) + 1;
									// Se obtiene la seccion
									WebElement seccionElement = driver.findElement(By.cssSelector(".row-ejercicio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
									// Se selecciona la seccion
									seccionElement.click();
									seleccionoEjercicio = true;
								}
							}
							// Se revisa que exista al menos un curso y se haya podido seleccionar
							if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
								// Se puede postula a la asignatura
								driver.switchTo().defaultContent();
								driver.switchTo().frame("mainFrame");
								driver.switchTo().frame(5);
								// Se postula a la asignatura
								driver.findElement(By.id("btn_postular")).click();
								TimeUnit.MILLISECONDS.sleep(350);
								if (driver.switchTo().alert().getText().compareTo(Vars_RA.alertaInscribirText) == 0 ) {
									driver.switchTo().alert().accept();
									TimeUnit.MILLISECONDS.sleep(350);
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame(5);
									// Se revisa el mensaje
									List<WebElement> mensajes = driver.findElements(By.cssSelector(".col-12 > div"));
									TimeUnit.MILLISECONDS.sleep(350);
									for (WebElement mensaje : mensajes){
										TimeUnit.MILLISECONDS.sleep(350);
										// Se revisa si el mensaje corresponde a asignatura inscrita
										if( mensaje.getText().compareTo(Vars_RA.postulacionStatusInscritaText + Integer.parseInt(listadoCodigoNombre2.get(rand_int1).get(0)) + " (" + Vars_RA.proceso + ")." ) == 0 ) {
											System.out.println("		Asignatura inscrita");
											// Se guarda la asignatura inscrita
											contadorAsignaturas++;
										} else {
											System.out.println("		Asignatura no inscrita");
										}
									}
								} else {
									System.out.println("No coincide el mensaje al inscribir la asignatura");
								}
								// Tiempo para guardar la asignatura postulada
								TimeUnit.MILLISECONDS.sleep(350);
							} else {
								System.out.println("		No es posible inscribir la asignatura");
							}
							// Se guarda el random para saber que esta asignatura no se debe repetir
							int codigo = Integer.parseInt(listadoCodigoNombre2.get(rand_int1).get(0));
							codigosAsignaturasAOmitir2.add(codigo);
						}
					}
				}
			}
			i = 1;
			System.out.println("	Se inscribieron " + contadorAsignaturas + " asignaturas");
			System.out.println("Se llego al limite de asignaturas a inscribir");
		}
		System.out.println("Se finaliza el test Test_3_Postulaciones_Inscribir_Limite\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	public void Test_4_Postulaciones_Desinscribir_Todo() throws InterruptedException {
		System.out.println("Se inicia el test Test_4_Postulaciones_Desinscribir_Todo");
		// Se configura el driver para firefox
		System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, Vars_RA.driverPath);
		System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY, Vars_RA.logPath);
		// Se crea el driver para navegar en la pagina web
		WebDriver driver = new FirefoxDriver();
		// Se abre la pagina
		driver.get(Vars_RA.url);
		driver.manage().window().maximize();
		// Login
		driver.findElement(By.id("rutaux")).click();
		driver.findElement(By.id("rutaux")).sendKeys(Vars_RA.userRUT);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("rutest")).click();
		driver.findElement(By.id("rutest")).sendKeys(Vars_RA.userRUTAlt);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.id("clave")).click();
		driver.findElement(By.id("clave")).sendKeys(Vars_RA.userPass);
		driver.findElement(By.cssSelector(".cover-container")).click();
		driver.findElement(By.cssSelector(".btn-lg")).click();
		TimeUnit.MILLISECONDS.sleep(350);
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de postulacion");
			System.out.println("Se finaliza el test Test_4_Postulaciones_Desinscribir_Todo\n\n==========================================================\n");
			driver.quit();
			return;
		}
		// Se revisa si el proceso esta abierto
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		try {
			driver.switchTo().frame("derecho");
		} catch (Exception e2) {
			System.out.println("El proceso se encuentra cerrado");
			System.out.println("Se finaliza el test Test_4_Postulaciones_Desinscribir_Todo\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Se entra al frame con las asignaturas postuladas
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se buscan la cantidad de asignaturas postuladas
		List<WebElement> accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		TimeUnit.MILLISECONDS.sleep(350);
		List<WebElement> codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		TimeUnit.MILLISECONDS.sleep(350);
		int cantidadPostulacionesDesinscribibles = 0;
		for( WebElement accion : accionesAsignaturasPostuladas ){
			if( accion.getText().equals(Vars_RA.btnPostulacionDESINSCRIBIRText) ){
				cantidadPostulacionesDesinscribibles++;
			}
		}
		if( cantidadPostulacionesDesinscribibles > 0 ){
			int i = 0;
			while( i == 0){
				int cantidadBotones = accionesAsignaturasPostuladas.size();
				int j = 0;
				while( j < cantidadBotones ){
					if( accionesAsignaturasPostuladas.get(j).getText().equals(Vars_RA.btnPostulacionDESINSCRIBIRText) ){
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = accionesAsignaturasPostuladas.get(j).getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						String codigo = codigosAsignaturasPostuladas.get(j).getText().split("-")[0].strip();
						// Se selecciona la solicitud
						accionesAsignaturasPostuladas.get(j).click();
						TimeUnit.MILLISECONDS.sleep(350);
						// Se acepa el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Tiempo para esperar que se abra la alerta
						TimeUnit.MILLISECONDS.sleep(350);
						// Se escribe el codigo de la asignatura
						driver.switchTo().alert().sendKeys(codigo);
						// Se acepta el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						//Se ingresa el codigo de la asignatura
						driver.switchTo().defaultContent();
						// Se revisa si se puede seleccionar el proceso de postulaciones
						driver.switchTo().defaultContent();
						driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
						try {
							driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
						} catch (Exception e) {
							System.out.println("No se encuentra el proceso de postulacion");
							System.out.println("Se finaliza el test Test_4_Postulaciones_Desinscribir_Todo\n\n==========================================================\n");
							driver.quit();
							return;
						}
						// Se revisa si el proceso esta abierto
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						try {
							driver.switchTo().frame("derecho");
						} catch (Exception e2) {
							System.out.println("El proceso se encuentra cerrado");
							System.out.println("Se finaliza el test Test_4_Postulaciones_Desinscribir_Todo\n\n==========================================================\n");
							driver.quit();
							return;
						}
						driver.switchTo().defaultContent();
						TimeUnit.MILLISECONDS.sleep(350);
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame(5);
						// Se buscan la cantidad de asignaturas restantes
						accionesAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
						TimeUnit.MILLISECONDS.sleep(350);
						codigosAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
						TimeUnit.MILLISECONDS.sleep(350);
						cantidadBotones = accionesAsignaturasPostuladas.size();
					} else {
						j++;
					}
				}
				i = 1;
			}
		} else {
			System.out.println("No hay asignaturas para desinscribir");
		}
		System.out.println("Se finaliza el test Test_4_Postulaciones_Desinscribir_Todo\n\n==========================================================\n");
		driver.quit();
	}
  
}