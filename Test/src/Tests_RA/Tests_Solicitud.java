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

// import org.junit.Rule;
// import org.junit.runner.RunWith;
// import com.google.code.tempusfugit.concurrency.ConcurrentRule;
// import com.google.code.tempusfugit.concurrency.ConcurrentTestRunner;
// import com.google.code.tempusfugit.concurrency.RepeatingRule;
// import com.google.code.tempusfugit.concurrency.annotations.Repeating;
// import com.google.code.tempusfugit.concurrency.annotations.Concurrent;

//@RunWith(ConcurrentTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests_Solicitud extends Vars_RA{
  @Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_1_Solicitudes_Inscribir_Random() throws InterruptedException {
		System.out.println("Se inicia el test Test_1_Solicitudes_Inscribir_Random");
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
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_1_Solicitudes_Inscribir_Random\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_1_Solicitudes_Inscribir_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame("derecho");
		// Se obtienen todas las asignaturas
		List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector("tr.bold7"));
		// Se almacenan todas las asignaturas
		ArrayList<String> listadoAsignaturasAux = new ArrayList<String>();
		for (WebElement e : listadoAsignaturas) {
			String[] asignatura = e.getText().split(" ");
			String tmp = "";
			for( int i = 1; i < asignatura.length - 1 ; i++ ) {
				tmp += " " + asignatura[i];
			}
			listadoAsignaturasAux.add(tmp.strip());
		}
		// Se revisan todos los cursos
		int i = 0;
		while( i == 0) {
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
				// Se crea el JavascriptExecutor para hacer scroll
				JavascriptExecutor js = (JavascriptExecutor) driver;
				// Se selecciona una asignatura
				WebElement asignatura = driver.findElement(By.linkText(listadoAsignaturasAux.get(rand_int1-1)));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se obtiene la posicion de la asignatura seleciconada
				Point location = asignatura.getLocation();
				// Se hace scroll hacia la asignatura
				js.executeScript("window.scrollBy(0,"+location.getY()+")");
				// Se hace click en la asignatura
				asignatura.click();
				System.out.println("\nAsignatura: " + asignatura.getText());
				TimeUnit.MILLISECONDS.sleep(350);
				System.out.println("	Se revisan los cursos de teoria");
				// Se sale del frame, y se entra al frame de los cursos de teoria.
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_cteo");
				// Se selecciona el frame de los cursos (:: COORDINACIONES CURSOS es un frame)
				driver.switchTo().frame("mainFrame");
				// Se obtienen todos los cursos
				List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se almacenan todas las secciones
				ArrayList<String> listadoTeoriaAux = new ArrayList<String>();
				for (WebElement e : listadoTeoria) {
					String tmp = e.getText().replace("\n", " ");
					if( tmp.equals(Vars_RA.sinCoordinacionSolicitudTeoriaText) ){
						// No hay cursos de teoria
						System.out.println("			No hay cursos de teoria");
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
				System.out.println("	Se revisan los cursos de laboratorio");
				// Se sale del frame, y se entra al frame de los cursos de laboratorio.
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_clab");
				// Se selecciona el frame de los cursos (::LISTADO LABORATORIOS es un frame)
				driver.switchTo().frame("mainFrame");
				// Se obtienen todos los cursos
				List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se almacenan todas las secciones
				ArrayList<String> listadoEjercicioAux = new ArrayList<String>();
				for (WebElement e : listadoEjercicio) {
					String tmp = e.getText().replace("\n", " ");
					// Los frames de laboratorio y ejercicio estan al revez, por lo que se esta revisando el frame de ejercicio
					if( tmp.equals(Vars_RA.sinCoordinacionSolicitudEjeText) ){
						// No hay cursos de laboratorio
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
				System.out.println("	Se revisan los cursos de ejercicio");
				// Se sale del frame, y se entra al frame de los cursos de ejercicio.
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("frame_ceje");
				// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
				driver.switchTo().frame("mainFrame");
				// Se obtienen todos los cursos
				List<WebElement> listadoLaboratorio = driver.findElements(By.cssSelector("tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se almacenan todas las secciones
				ArrayList<String> listadoLaboratorioAux = new ArrayList<String>();
				for (WebElement e : listadoLaboratorio) {
					String tmp = e.getText().replace("\n", " ");
					// Los frames de laboratorio y ejercicio estan al revez, por lo que se esta revisando el frame de ejercicio
					if( tmp.equals(Vars_RA.sinCoordinacionSolicitudLabText) ){
						// No hay cursos de ejercicios
					} else {
						// Existen coordinaciones
						existeLaboratorio = true;
						// Se crea un random para seleccionar una seccion de teoria al azar
						int rand_int2 = rand.nextInt((listadoLaboratorio.size() - 1) + 1) + 1;
						// Se obtiene la seccion
						WebElement seccionElement = driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
						// Se selecciona la seccion
						seccionElement.click();
						seleccionoLaboratorio = true;
						System.out.println("			Si hay cursos de teoria");
						listadoLaboratorioAux.add(tmp);
					}
				}
				System.out.println("	Se revisa que se haya seleccionado al menos un curso");
				// Se revisa que exista al menos un curso
				if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
					// Se puede postular a la asignatura
					System.out.println("	Se puede postular a la asignatura");
					// Se sale del frame, y se entra al frame deonde esta el boton de postular
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("frame_solicitudes");
					// Se ingresa el motivo de la postulacion
					driver.findElement(By.id("motivo")).click();
					driver.findElement(By.id("motivo")).sendKeys("Motivo Test");
					// Se postula a la asignatura
					driver.findElement(By.id("btn_solicitar")).click();
					TimeUnit.MILLISECONDS.sleep(350);
					if (driver.switchTo().alert().getText().compareTo(Vars_RA.alertaSolicitudConfirmText) == 0 ) {
						driver.switchTo().alert().accept();
					}
					// Tiempo para guardar la asignatura postulada
					TimeUnit.MILLISECONDS.sleep(350);
					i = 1;
				} else {
					// No se puede postular a la asignatura
					System.out.println("	No se puede postular a la asignatura");
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
				}
			}
		}
		System.out.println("Se finaliza el test Test_1_Solicitudes_Inscribir_Random\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_2_Solicitudes_Revisar_Random() throws InterruptedException {
		System.out.println("Se inicia el test Test_2_Solicitudes_Revisar_Random");
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
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_2_Solicitudes_Revisar_Random\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_2_Solicitudes_Revisar_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		TimeUnit.MILLISECONDS.sleep(350);
		// Se obtienen los ids de las solicitudes revisables
		ArrayList<Integer> listadoSolicitudesRevisables = new ArrayList<Integer>();
		for( int i = 0; i < listadoSolicitudesEnviadas.size(); i++ ) {
			if( listadoSolicitudesEnviadas.get(i).getText().equals(Vars_RA.btnSolicitudRESPUESTAText) ){
				listadoSolicitudesRevisables.add(i);
			}
		}
		if( listadoSolicitudesRevisables.size() <= 0 ) {
			System.out.println("No hay solicitudes revisables");
		} else {
			// Se crea un random para seleccionar una solicitud revisable al azar
			Random rand = new Random();
			int rand_int = rand.nextInt((listadoSolicitudesRevisables.get(rand.nextInt(listadoSolicitudesRevisables.size())) - 1) + 1) + 1;
			// Se crea el JavascriptExecutor para hacer scroll
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Se obtiene la posicion del boton
			Point location = listadoSolicitudesEnviadas.get(rand_int).getLocation();
			// Se hace scroll hacia el boton
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se abre la solicitud
			listadoSolicitudesEnviadas.get(rand_int).click();
			// Tiempo para que se abra la ventana emergente
			TimeUnit.MILLISECONDS.sleep(350);
			// Se obtienen las lineas de respuesta
			List<WebElement> lineasRespuesta = driver.findElements(By.cssSelector("#motivo_rechazo_"+rand_int+" > table:nth-child(1) > tbody > tr"));
			TimeUnit.MILLISECONDS.sleep(350);
			// Se obtiene el codigo y nombre de la asignatura de la solicitud
			System.out.println("Solicitud:");
			String codigoAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[0];
			String estadoSolictudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[ lineasRespuesta.get(0).getText().strip().split(" ").length - 1 ];
			String nombreAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().substring(codigoAsignaturaSolicitudRevisada.length(), lineasRespuesta.get(0).getText().strip().length() - estadoSolictudRevisada.length() - 1).strip();
			System.out.println("	----------------------------------------");
			System.out.println("	Seccion:    " + codigoAsignaturaSolicitudRevisada);
			System.out.println("	Asignatura: " + nombreAsignaturaSolicitudRevisada);
			System.out.println("	Estado:     " + estadoSolictudRevisada);
			System.out.println("	----------------------------------------");
			String[] lineaRespuestasStrings = lineasRespuesta.get(1).getText().strip().split("\n");
			for( int i = 0; i < lineaRespuestasStrings.length - 1; i++ ) {
				System.out.println("		" + lineaRespuestasStrings[i].strip());
			}
			System.out.println("	----------------------------------------");
			// Se revisa si se puede seleccionar el proceso de postulaciones
			driver.switchTo().defaultContent();
			driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
			try {
				driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
			} catch (Exception e) {
				System.out.println("No se encuentra el proceso de solicitudes de inscripción");
				System.out.println("Se finaliza el test Test_2_Solicitudes_Revisar_Random\n\n==========================================================\n");
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
				System.out.println("Se finaliza el test Test_2_Solicitudes_Revisar_Random\n\n==========================================================\n");
				driver.quit();
				return;
			}
			driver.switchTo().defaultContent();
			TimeUnit.MILLISECONDS.sleep(350);
		}
		System.out.println("Se finaliza el test Test_2_Solicitudes_Revisar_Random\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_3_Solicitudes_Eliminar_Random() throws InterruptedException {
		System.out.println("Se inicia el test Test_3_Solicitudes_Eliminar_Random");
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
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_3_Solicitudes_Eliminar_Random\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_3_Solicitudes_Eliminar_Random\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		TimeUnit.MILLISECONDS.sleep(350);
		// Se obtienen los ids de las solicitudes eliminables
		ArrayList<Integer> listadoSolicitudesEliminables = new ArrayList<Integer>();
		for( int i = 0; i < listadoSolicitudesEnviadas.size(); i++ ) {
			if( listadoSolicitudesEnviadas.get(i).getText().equals(Vars_RA.btnPostulacionELIMINARText) ){
				listadoSolicitudesEliminables.add(i);
			}
		}
		if( listadoSolicitudesEliminables.size() > 0 ){
			// Se crea un random para seleccionar una solicitud eliminable al azar
			Random rand = new Random();
			int rand_int = rand.nextInt(       (listadoSolicitudesEliminables.size() - 1) + 1       ) + 1;
			// Se crea el JavascriptExecutor para hacer scroll
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Se obtiene la posicion del boton
			Point location = listadoSolicitudesEnviadas.get(rand_int-1).getLocation();
			// Se hace scroll hacia el boton
			js.executeScript("window.scrollBy(0,"+location.getY()+")");
			// Se elimina la solicitud
			listadoSolicitudesEnviadas.get(rand_int-1).click();
			TimeUnit.MILLISECONDS.sleep(350);
			driver.switchTo().alert().accept();
		} else {
			System.out.println("No hay solicitudes eliminables");
		}
		System.out.println("Se finaliza el test Test_3_Solicitudes_Eliminar_Random\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_4_Solicitudes_Inscribir_Limite() throws InterruptedException {
		System.out.println("Se inicia el test Test_4_Solicitudes_Inscribir_Limite");
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

																	int CANTIDADASIGNATURASLIMITE = 8;

		// Se busca cuantas asignaturas lleva solicitadas y postuladas
		int contadorAsignaturas = 0;
		System.out.println("Contador asignaturas 1: "+contadorAsignaturas);
		Boolean procesoPostulaciones = true;
		Boolean procesoInscripcion = true;

		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar proceso postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
		} catch (Exception e) {
			System.out.println("	No se encuentra el proceso de postulacion");
			procesoPostulaciones = false;
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
			procesoInscripcion = false;
			driver.navigate().refresh();
		}
		TimeUnit.MILLISECONDS.sleep(350);
		// Si esta abierto el proceso de postulaciones, se cuentan cuantas postulaciones hay
		if ( procesoPostulaciones ) {
			driver.switchTo().defaultContent();
			driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
			driver.findElement(By.linkText(Vars_RA.postulacionText)).click();
			TimeUnit.MILLISECONDS.sleep(350);
			// Seleccionar el frame con las postulaciones
			driver.switchTo().defaultContent();
			driver.switchTo().frame("mainFrame");
			driver.switchTo().frame("frame_inscripcion");
			// Se obtienen todas las asignaturas
			List<WebElement> listadoAsignaturasPostuladas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
			// Se cuenta cuantas asignaturas se han postulado
			ArrayList<String> codigosPostulaciones = new ArrayList<String>();
			int i = 0;
			while ( i < listadoAsignaturasPostuladas.size() ){
				String tmp = listadoAsignaturasPostuladas.get(i).getText().split("-")[0].trim();
				if( !codigosPostulaciones.contains(tmp) ){
					codigosPostulaciones.add(tmp);
				}
				i++;
			}
			contadorAsignaturas += codigosPostulaciones.size();
		}
		// Si esta abierto el proceso de inscripciones, se cuentan cuantas inscripciones hay
		if ( procesoInscripcion ) {
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
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las solicitudes enviadas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(1)"));
		TimeUnit.MILLISECONDS.sleep(350);
		contadorAsignaturas += listadoSolicitudesEnviadas.size();
		System.out.println("Contador asignaturas 4: "+contadorAsignaturas);
		int j = 0;
		while( j == 0){
			// Se dan 3 vueltas para probar inscribir las asignaturas
			for( int k = 1; k <= 3; k++ ){
				System.out.println("Vuelta " + k );
				// Se revisa si se puede seleccionar el proceso de postulaciones
				driver.switchTo().defaultContent();
				driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
				try {
					driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
				} catch (Exception e) {
					System.out.println("No se encuentra el proceso de solicitudes de inscripción");
					System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
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
					System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
					driver.quit();
					return;
				}
				driver.switchTo().defaultContent();
				TimeUnit.MILLISECONDS.sleep(350);
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame("derecho");
				// Se obtienen todas las asignaturas
				List<WebElement> listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				ArrayList<ArrayList<String>> listadoCodigoNombre = new ArrayList<ArrayList<String>>();
				for (WebElement linea : listadoAsignaturas) {
					String codigo = linea.getText().split(" ")[0];
					String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
					listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
				}
				// Se crea una lista para almacenar las asignaturas que no se pudieron inscribir
				ArrayList<Integer> codigosAsignaturasAOmitir = new ArrayList<Integer>();
				// Se revisan todos los cursos
				int l = 0;
				System.out.println("Contador asignaturas 5: "+contadorAsignaturas);
				while( (l == 0) && (contadorAsignaturas < CANTIDADASIGNATURASLIMITE) ) {
					// Seleccionar frame del listado de cursos
					driver.switchTo().defaultContent();
					driver.switchTo().frame("mainFrame");
					driver.switchTo().frame("derecho");
					listadoAsignaturas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr"));
					TimeUnit.MILLISECONDS.sleep(350);
					listadoCodigoNombre = new ArrayList<ArrayList<String>>();
					for (WebElement linea : listadoAsignaturas) {
						String codigo = linea.getText().split(" ")[0];
						String nombreAsignatura = linea.getText().substring( codigo.length(), linea.getText().length() - 1 ).strip();
						listadoCodigoNombre.add(new ArrayList<String>(Arrays.asList(codigo, nombreAsignatura)));
					}
					if( listadoCodigoNombre.size() == 0 ){
						System.out.println("	No hay asignaturas disponibles para inscribirse");
						l = 1;
					} else {
						Boolean existeTeoria = false;
						Boolean existeLaboratorio = false;
						Boolean existeEjercicio = false;
						Boolean seleccionoTeoria = false;
						Boolean seleccionoLaboratorio = false;
						Boolean seleccionoEjercicio = false;
						// Se revisa que existan asignaturas
						if( listadoCodigoNombre.size() == 0 ) {
							System.out.println("No hay mas asignaturas");
							l = 1;
						} else {
							// Se crea el random para seleccionar una asignatura al azar
							Random rand = new Random();
							int rand_int1 = rand.nextInt((listadoCodigoNombre.size() - 1) + 1) + 1;
							// Se revisa cuantas asignaturas se omitieron
							int asignaturasOmitidas = 0;
							// Mientras el while este en la lista de omitidos y no se alcance el limite, se busca otro
							while( (codigosAsignaturasAOmitir.contains(Integer.parseInt(listadoCodigoNombre.get(rand_int1-1).get(0)))) && (asignaturasOmitidas <= listadoCodigoNombre.size() ) ){
								rand_int1 = rand.nextInt((listadoCodigoNombre.size() - 1) + 1) + 1;
								asignaturasOmitidas++;
							}
							// Si se revisaron todas las asignaturas en una vuelta, sale de la vuelta
							if( asignaturasOmitidas > listadoCodigoNombre.size() ){
								l = 1;
							} else {
								// Se revisa si se puede seleccionar el proceso de postulaciones
								driver.switchTo().defaultContent();
								driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
								try {
									driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
								} catch (Exception e) {
									System.out.println("No se encuentra el proceso de solicitudes de inscripción");
									System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
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
									System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
									driver.quit();
									return;
								}
								driver.switchTo().defaultContent();
								TimeUnit.MILLISECONDS.sleep(350);
								driver.switchTo().defaultContent();
								driver.switchTo().frame("mainFrame");
								driver.switchTo().frame("derecho");
								// Se busca la asignatura
								WebElement asignatura = driver.findElement(By.cssSelector("tr.bold7:nth-child("+(rand_int1)+") > td:nth-child(2) > a:nth-child(1)"));
								// Se crea el JavascriptExecutor para hacer scroll
								JavascriptExecutor js = (JavascriptExecutor) driver;
								// Se obtiene la posicion del boton
								Point location = asignatura.getLocation();
								// Se hace scroll hacia el boton
								js.executeScript("window.scrollBy(0,"+location.getY()+")");
								// Se hace click en la asignatura
								asignatura.click();
								System.out.println("\n	Asignatura: " + driver.findElement(By.cssSelector("tr.bold7:nth-child("+(rand_int1)+") > td:nth-child(2) > a:nth-child(1)")).getText() );
								TimeUnit.MILLISECONDS.sleep(350);
								// Se sale del frame, y se entra al frame de los cursos de teoria.
								driver.switchTo().defaultContent();
								driver.switchTo().frame("mainFrame");
								driver.switchTo().frame("frame_cteo");
								// Se selecciona el frame de los cursos (::LISTADO CURSOS es un frame)
								driver.switchTo().frame("mainFrame");
								System.out.println("		Se revisan los cursos de teoria");
								TimeUnit.MILLISECONDS.sleep(350);
								// Se obtienen todos los cursos
								List<WebElement> listadoTeoria = driver.findElements(By.cssSelector("tr"));
								TimeUnit.MILLISECONDS.sleep(350);
								// Se obtiene la primera linea despues de buscar tr
								String primeraLineaTeoria = listadoTeoria.get(0).getText().strip();
								// Si la primera linea dice que no hay coordinaciones, se avisa
								if( !primeraLineaTeoria.equals(Vars_RA.sinCoordinacionSolicitudTeoriaText) ){
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
								System.out.println("		Se revisan los cursos de ejercicio");
								TimeUnit.MILLISECONDS.sleep(350);
								// Se obtienen todos los cursos
								List<WebElement> listadoEjercicio = driver.findElements(By.cssSelector("tr"));
								TimeUnit.MILLISECONDS.sleep(350);
								// Se obtiene la primera linea despues de buscar tr
								String primeraLineaEje = listadoEjercicio.get(0).getText().strip();
								// Si la primera linea dice que no hay coordinaciones, se avisa
								if( !primeraLineaEje.equals(Vars_RA.sinCoordinacionSolicitudEjeText) ){
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
									System.out.println("			Si hay cursos de ejercicios");
								} else {
									// No hay cursos de teoria
									System.out.println("			No hay cursos de ejercicios");
								}
								// Se sale del frame, y se entra al frame de los cursos de ejercicio.
								driver.switchTo().defaultContent();
								driver.switchTo().frame("mainFrame");
								driver.switchTo().frame("frame_ceje");
								// Se selecciona el frame de los cursos (::LISTADO EJERCICIOS es un frame)
								driver.switchTo().frame("mainFrame");
								System.out.println("		Se revisan los cursos de laboratorio");
								TimeUnit.MILLISECONDS.sleep(350);
								// Se obtienen todos los cursos
								List<WebElement> listadoLab = driver.findElements(By.cssSelector("tr"));
								TimeUnit.MILLISECONDS.sleep(350);
								// Se obtiene la primera linea despues de buscar tr
								String primeraLineaLab = listadoLab.get(0).getText().strip();
								// Si la primera linea dice que no hay coordinaciones, se avisa
								if( !primeraLineaLab.equals(Vars_RA.sinCoordinacionSolicitudLabText) ){
									// Existen coordinaciones
									existeLaboratorio = true;
									// Se crea un random para seleccionar una seccion de teoria al azar
									int rand_int2 = rand.nextInt((listadoLab.size() - 1) + 1) + 1;
									// Se obtiene la seccion
									WebElement seccionElement = driver.findElement(By.cssSelector(".row-laboratorio:nth-child("+(rand_int2)+") > td:nth-child(1)"));
									// Se hace scroll hacia el boton
									js.executeScript("window.scrollBy(0,"+seccionElement.getLocation().getY()+")");
									// Se selecciona la seccion
									seccionElement.click();
									seleccionoLaboratorio = true;
									System.out.println("			Si hay cursos de laboratorio");
								} else {
									// No hay cursos de teoria
									System.out.println("			No hay cursos de laboratorio");
								}
								if( (existeTeoria && seleccionoTeoria) || (existeLaboratorio && seleccionoLaboratorio) || (existeEjercicio && seleccionoEjercicio) ) {
									// Se puede postular a la asignatura
									System.out.println("	Se puede solicitar la asignatura");
									// Se sale del frame, y se entra al frame deonde esta el boton de postular
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame("frame_solicitudes");
									// Se ingresa el motivo de la postulacion
									driver.findElement(By.id("motivo")).click();
									driver.findElement(By.id("motivo")).sendKeys("Motivo Test");
									// Se postula a la asignatura
									driver.findElement(By.id("btn_solicitar")).click();
									TimeUnit.MILLISECONDS.sleep(350);
									if (driver.switchTo().alert().getText().compareTo(Vars_RA.alertaSolicitudConfirmText) == 0 ) {
										driver.switchTo().alert().accept();
									}
									contadorAsignaturas++;
									// Tiempo para guardar la asignatura postulada
									TimeUnit.MILLISECONDS.sleep(350);
								} else {
									// No se puede postular a la asignatura
									System.out.println("	No se puede postular a la asignatura");
									
									driver.switchTo().defaultContent();
									driver.switchTo().frame("mainFrame");
									driver.switchTo().frame("derecho");
								}
								// Se guarda el random para saber que esta asignatura no se debe repetir
								int codigo = Integer.parseInt(listadoCodigoNombre.get(rand_int1-1).get(0));
								codigosAsignaturasAOmitir.add(codigo);
							}
						}
					}
				}
			}
			j = 1;
			System.out.println("	Se inscribieron " + contadorAsignaturas + " asignaturas");
			System.out.println("Se llego al limite de asignaturas a inscribir");
		}
		System.out.println("Se finaliza el test Test_4_Solicitudes_Inscribir_Limite\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_5_Solicitudes_Revisar_Todo() throws InterruptedException {
		System.out.println("Se inicia el test Test_5_Solicitudes_Revisar_Todo");
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
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		driver.switchTo().frame(5);
		// Se obtienen todas las asignaturas
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		TimeUnit.MILLISECONDS.sleep(350);
		// Se obtienen los ids de las solicitudes revisables
		ArrayList<Integer> listadoSolicitudesRevisables = new ArrayList<Integer>();
		for( int i = 0; i < listadoSolicitudesEnviadas.size(); i++ ) {
			if( listadoSolicitudesEnviadas.get(i).getText().equals(Vars_RA.btnSolicitudRESPUESTAText) ){
				listadoSolicitudesRevisables.add(i);
			}
		}
		if( listadoSolicitudesRevisables.size() <= 0 ){
			System.out.println("No hay solicitudes revisables");
		} else {
			// Se crea el JavascriptExecutor para hacer scroll
			JavascriptExecutor js = (JavascriptExecutor) driver;
			for( int solicitud : listadoSolicitudesRevisables ){
				// Se revisa si se puede seleccionar el proceso de postulaciones
				driver.switchTo().defaultContent();
				driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
				try {
					driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
				} catch (Exception e) {
					System.out.println("No se encuentra el proceso de solicitudes de inscripción");
					System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
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
					System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
					driver.quit();
					return;
				}
				driver.switchTo().defaultContent();
				TimeUnit.MILLISECONDS.sleep(350);
				driver.switchTo().defaultContent();
				driver.switchTo().frame("mainFrame");
				driver.switchTo().frame(5);
				TimeUnit.MILLISECONDS.sleep(350);
				// Se obtienen todas las asignaturas
				List<WebElement> listadoSolicitudesEnviadasAux = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se obtiene la posicion del boton
				Point location = listadoSolicitudesEnviadasAux.get(solicitud).getLocation();
				// Se hace scroll hacia el boton
				js.executeScript("window.scrollBy(0,"+location.getY()+")");
				// Se abre la solicitud
				TimeUnit.MILLISECONDS.sleep(350);
				listadoSolicitudesEnviadasAux.get(solicitud).click();
				// Tiempo para que se abra la ventana emergente
				TimeUnit.MILLISECONDS.sleep(350);
				// Se obtienen las lineas de respuesta
				List<WebElement> lineasRespuesta = driver.findElements(By.cssSelector("#motivo_rechazo_"+solicitud+" > table:nth-child(1) > tbody > tr"));
				TimeUnit.MILLISECONDS.sleep(350);
				// Se obtiene el codigo y nombre de la asignatura de la solicitud
				System.out.println("Solicitud:");
				String codigoAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[0];
				String estadoSolictudRevisada = lineasRespuesta.get(0).getText().strip().split(" ")[ lineasRespuesta.get(0).getText().strip().split(" ").length - 1 ];
				String nombreAsignaturaSolicitudRevisada = lineasRespuesta.get(0).getText().substring(codigoAsignaturaSolicitudRevisada.length(), lineasRespuesta.get(0).getText().strip().length() - estadoSolictudRevisada.length() - 1).strip();
				System.out.println("	----------------------------------------");
				System.out.println("	Seccion:    " + codigoAsignaturaSolicitudRevisada);
				System.out.println("	Asignatura: " + nombreAsignaturaSolicitudRevisada);
				System.out.println("	Estado:     " + estadoSolictudRevisada);
				System.out.println("	----------------------------------------");
				String[] lineaRespuestasStrings = lineasRespuesta.get(1).getText().strip().split("\n");
				for( int i = 0; i < lineaRespuestasStrings.length - 1; i++ ) {
					System.out.println("		" + lineaRespuestasStrings[i].strip());
				}
				System.out.println("	----------------------------------------");
				// Se revisa si se puede seleccionar el proceso de postulaciones
				driver.switchTo().defaultContent();
				driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
				try {
					driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
				} catch (Exception e) {
					System.out.println("No se encuentra el proceso de solicitudes de inscripción");
					System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
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
					System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
					driver.quit();
					return;
				}
				driver.switchTo().defaultContent();
				TimeUnit.MILLISECONDS.sleep(350);
			}
		}
		// Se revisa si se puede seleccionar el proceso de postulaciones
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
		try {
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_5_Solicitudes_Revisar_Todo\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		System.out.println("Se finaliza el test Solicitudes_Revisar_Todo\n\n==========================================================\n");
		driver.quit();
	}

	@Test
	//@Concurrent (count = 2)
	//@Repeating (repetition = 2)
	public void Test_6_Solicitudes_Eliminar_Todo() throws InterruptedException {
		System.out.println("Se inicia el test Test_6_Solicitudes_Eliminar_Todo");
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
			driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
		} catch (Exception e) {
			System.out.println("No se encuentra el proceso de solicitudes de inscripción");
			System.out.println("Se finaliza el test Test_6_Solicitudes_Eliminar_Todo\n\n==========================================================\n");
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
			System.out.println("Se finaliza el test Test_6_Solicitudes_Eliminar_Todo\n\n==========================================================\n");
			driver.quit();
			return;
		}
		driver.switchTo().defaultContent();
		TimeUnit.MILLISECONDS.sleep(350);
		// Seleccionar frame del listado de cursos
		driver.switchTo().defaultContent();
		driver.switchTo().frame("mainFrame");
		TimeUnit.MILLISECONDS.sleep(350);
		driver.switchTo().frame(5);
		// Se obtienen todas las postulaciones
		List<WebElement> listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
		TimeUnit.MILLISECONDS.sleep(350);
		int cantidadSolicitudesEliminables = 0;
		for( WebElement solicitud : listadoSolicitudesEnviadas ){
			if( solicitud.getText().equals(Vars_RA.btnSolicitudELIMINARText) ){
				cantidadSolicitudesEliminables++;
			}
		}
		if( cantidadSolicitudesEliminables > 0 ){
			int i = 0;
			while( i == 0){
				int cantidadBotones = listadoSolicitudesEnviadas.size();
				int j = 0;
				while( j < cantidadBotones ){
					if( listadoSolicitudesEnviadas.get(j).getText().equals(Vars_RA.btnPostulacionELIMINARText) ){
						// Se crea el JavascriptExecutor para hacer scroll
						JavascriptExecutor js = (JavascriptExecutor) driver;
						// Se obtiene la posicion del boton
						Point location = listadoSolicitudesEnviadas.get(j).getLocation();
						// Se hace scroll hacia el boton
						js.executeScript("window.scrollBy(0,"+location.getY()+")");
						// Se selecciona la solicitud
						listadoSolicitudesEnviadas.get(j).click();
						TimeUnit.MILLISECONDS.sleep(350);
						// Se acepa el mensaje de desinscripcion
						driver.switchTo().alert().accept();
						// Se revisa si se puede seleccionar el proceso de postulaciones
						driver.switchTo().defaultContent();
						driver.findElement(By.cssSelector("li.dropdown:nth-child(3) > a:nth-child(1)")).click();
						try {
							driver.findElement(By.linkText(Vars_RA.solicitudText)).click();
						} catch (Exception e) {
							System.out.println("No se encuentra el proceso de solicitudes de inscripción");
							System.out.println("Se finaliza el test Test_6_Solicitudes_Eliminar_Todo\n\n==========================================================\n");
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
							System.out.println("Se finaliza el test Test_6_Solicitudes_Eliminar_Todo\n\n==========================================================\n");
							driver.quit();
							return;
						}
						driver.switchTo().defaultContent();
						TimeUnit.MILLISECONDS.sleep(350);
						driver.switchTo().defaultContent();
						driver.switchTo().frame("mainFrame");
						driver.switchTo().frame(5);
						// Se buscan la cantidad de asignaturas restantes
						listadoSolicitudesEnviadas = driver.findElements(By.cssSelector(".table > tbody:nth-child(2) > tr > td:nth-child(7)"));
						TimeUnit.MILLISECONDS.sleep(350);
						cantidadBotones = listadoSolicitudesEnviadas.size();
					} else {
						j++;
					}
				}
				i = 1;
			}
		} else {
			System.out.println("No hay solicitudes eliminables");
		}
		System.out.println("Se finaliza el test Solicitudes_Eliminar_Todo\n\n==========================================================\n");
		driver.quit();
	}
  
}