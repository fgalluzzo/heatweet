package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import store.AccessTokenStore;

import model.Value;

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

@Path("fusion")
public class FusionController {
	private static final Pattern CSV_VALUE_PATTERN = Pattern
			.compile("([^,\\r\\n\"]*|\"(([^\"]*\"\")*[^\"]*)\")(,|\\r?\\n)");	
	private static final String FUSION_SERVICE_URL = "https://www.google.com/fusiontables/api/query";
	GoogleService service;
	URL url;

	

	@GET
	@Path(value = "create")
	public String createTable(@QueryParam("param") String param) {
		try {
			this.url = new URL(FUSION_SERVICE_URL);
			service = AccessTokenStore.authenticate();
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.INSERT, this.url,
					new ContentType("application/x-www-form-urlencoded"));
			OutputStreamWriter writer = new OutputStreamWriter(
					request.getRequestStream());
			writer.append("sql=" + URLEncoder.encode(param, "UTF-8"));
			writer.flush();

			request.execute();

			String decoded = new String();
			Scanner scanner = new Scanner(request.getResponseStream(), "UTF-8");
			while (scanner.hasNextLine()) {
				scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
				MatchResult match = scanner.match();
				String quotedString = match.group(2);
				decoded = quotedString == null ? match.group(1) : quotedString
						.replaceAll("\"\"", "\"");
				System.out.print("|" + decoded);
				if (!match.group(4).equals(",")) {
					System.out.println("|");
				}
			}
			return decoded;

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOK";

	}

	@GET
	@Path(value = "insert")
	public String insert(@QueryParam("tb") String table,
			@QueryParam("c") String columns, @QueryParam("v") String values) {
		try {
			this.url = new URL(FUSION_SERVICE_URL);

			service = AccessTokenStore.authenticate();
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.INSERT, this.url,
					new ContentType("application/x-www-form-urlencoded"));
			OutputStreamWriter writer = new OutputStreamWriter(
					request.getRequestStream());
			writer.append("sql="
					+ URLEncoder.encode("INSERT INTO " + table + " (" + columns
							+ ") VALUES (" + values + ")", "UTF-8"));
			writer.flush();

			request.execute();

			String decoded = new String();
			Scanner scanner = new Scanner(request.getResponseStream(), "UTF-8");
			while (scanner.hasNextLine()) {
				scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
				MatchResult match = scanner.match();
				String quotedString = match.group(2);
				decoded = quotedString == null ? match.group(1) : quotedString
						.replaceAll("\"\"", "\"");
				System.out.print("|" + decoded);
				if (!match.group(4).equals(",")) {
					System.out.println("|");
				}
			}
			return decoded;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOK";

	}

	public String batchInsert(List<Value> values) {

		try {
			this.url = new URL(FUSION_SERVICE_URL);

			service = AccessTokenStore.authenticate();
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.INSERT, this.url,
					new ContentType("application/x-www-form-urlencoded"));
			OutputStreamWriter writer;

			writer = new OutputStreamWriter(request.getRequestStream());
			writer.append("sql=");
			Iterator<Value> iter = values.iterator();
			while (iter.hasNext()) {
				Value value = iter.next();
				String q = URLEncoder.encode(
						"INSERT INTO 2228772 (tweet,Location) VALUES (' "
								+ value.getTweet() + " ','"
								+ value.getLocation() + "');", "UTF-8");

				writer.append(q);
			}

			writer.flush();

			request.execute();

			String decoded = new String();
			Scanner scanner = new Scanner(request.getResponseStream(), "UTF-8");
			while (scanner.hasNextLine()) {
				scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
				MatchResult match = scanner.match();
				String quotedString = match.group(2);
				decoded = quotedString == null ? match.group(1) : quotedString
						.replaceAll("\"\"", "\"");
				System.out.print("|" + decoded);
				if (!match.group(4).equals(",")) {
					System.out.println("|");
				}
			}
			return decoded;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOK";
	}

	public String batchInsert(String table, List<Value> values, Calendar cal) {

		try {
			this.url = new URL(FUSION_SERVICE_URL);

			service = AccessTokenStore.authenticate();
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.INSERT, this.url,
					new ContentType("application/x-www-form-urlencoded"));
			OutputStreamWriter writer;

			writer = new OutputStreamWriter(request.getRequestStream());
			writer.append("sql=");
			Iterator<Value> iter = values.iterator();

			String data = cal.get(Calendar.DAY_OF_MONTH) + "/"
					+ cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
			String hora = cal.get(Calendar.HOUR_OF_DAY) + ":"
					+ cal.get(Calendar.MINUTE);
			while (iter.hasNext()) {
				Value value = iter.next();

				String q = URLEncoder.encode(
						"INSERT INTO " + table
								+ " (Text,Location,Date) VALUES ('"
								+ value.getTweet().replace("'", "") + "','"
								+ value.getLocation() + "','" + data + " "
								+ hora + "');", "UTF-8");

				writer.append(q);
			}

			writer.flush();

			request.execute();

			String decoded = new String();
			Scanner scanner = new Scanner(request.getResponseStream(), "UTF-8");
			while (scanner.hasNextLine()) {
				scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
				MatchResult match = scanner.match();
				String quotedString = match.group(2);
				decoded = quotedString == null ? match.group(1) : quotedString
						.replaceAll("\"\"", "\"");
				System.out.print("|" + decoded);
				if (!match.group(4).equals(",")) {
					System.out.println("|");
				}
			}
			return decoded;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOK";
	}

	@GET
	@Path(value = "deleteAll")
	public String deleteAll(@QueryParam("table") String table) {
		try {
			this.url = new URL(FUSION_SERVICE_URL);

			service = AccessTokenStore.authenticate();
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.INSERT, this.url,
					new ContentType("application/x-www-form-urlencoded"));
			OutputStreamWriter writer;

			writer = new OutputStreamWriter(request.getRequestStream());

			writer.append("sql="
					+ URLEncoder.encode("DELETE FROM " + table, "UTF-8"));
			writer.flush();

			request.execute();

			String decoded = new String();
			Scanner scanner = new Scanner(request.getResponseStream(), "UTF-8");
			while (scanner.hasNextLine()) {
				scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
				MatchResult match = scanner.match();
				String quotedString = match.group(2);
				decoded = quotedString == null ? match.group(1) : quotedString
						.replaceAll("\"\"", "\"");
				System.out.print("|" + decoded);
				if (!match.group(4).equals(",")) {
					System.out.println("|");
				}
			}
			return decoded;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOK";

	}

	@GET
	@Path(value = "selectByLocation")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectByLocation(@QueryParam("table") String table) {
		try {
			String query = "?sql="
					+ URLEncoder.encode(
							"SELECT  Location, count(location) FROM " + table
									+ " GROUP BY Location", "UTF-8");
			this.url = new URL(FUSION_SERVICE_URL + query);

			service = AccessTokenStore.authenticate();

			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.QUERY, this.url, ContentType.TEXT_PLAIN);

			request.execute();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getResponseStream()));
			String line;
			StringWriter conteudo = new StringWriter();
			while ((line = br.readLine()) != null) {
				conteudo.append(line + ";");
			}
			/*
			 * String decoded = new String(); Scanner scanner = new
			 * Scanner(request.getResponseStream(), "UTF-8"); while
			 * (scanner.hasNextLine()) {
			 * scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0); MatchResult
			 * match = scanner.match(); String quotedString = match.group(2);
			 * decoded = quotedString == null ? match.group(1) : quotedString
			 * .replaceAll("\"\"", "\""); System.out.print("|" + decoded); if
			 * (!match.group(4).equals(",")) { System.out.println("|"); } }
			 */
			System.out.println(conteudo.toString());
			return conteudo.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@GET
	@Path(value = "selectDates")
	@Produces(MediaType.TEXT_PLAIN)
	public String selectDates(@QueryParam("table") String table) {
		try {
			service = AccessTokenStore.authenticate();
			String query = "?sql="
					+ URLEncoder.encode("SELECT Date, count() FROM " + table+" GROUP BY Date Order By Date", "UTF-8");
			this.url = new URL(FUSION_SERVICE_URL + query);
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.QUERY, this.url, ContentType.TEXT_PLAIN);
			request.execute();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getResponseStream()));			
			String line;
			StringWriter conteudo = new StringWriter();
			while ((line = br.readLine()) != null) {
				conteudo.append(line + ";");
			}
			System.out.println(conteudo.toString());
			return conteudo.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@GET
	@Path(value = "selectByLocationByDate")
	@Produces(MediaType.TEXT_PLAIN)
	public String selectByLocationByDate(@QueryParam("table") String table,@QueryParam("date") String date) {
		try {		
			
			service = AccessTokenStore.authenticate();
			String query = "?sql="
					+ URLEncoder.encode("SELECT  Location, count() FROM "
							+ table + " WHERE Date  ='"+date+"' GROUP BY Location", "UTF-8");			
			this.url = new URL(FUSION_SERVICE_URL + query);
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.QUERY, this.url, ContentType.TEXT_PLAIN);			
			
			request.execute();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getResponseStream()));

		

	
			String line;
			StringWriter conteudo = new StringWriter();
			while ((line = br.readLine()) != null) {
				conteudo.append(line + ";");
			}
			/*
			 * String decoded = new String(); Scanner scanner = new
			 * Scanner(request.getResponseStream(), "UTF-8"); while
			 * (scanner.hasNextLine()) {
			 * scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0); MatchResult
			 * match = scanner.match(); String quotedString = match.group(2);
			 * decoded = quotedString == null ? match.group(1) : quotedString
			 * .replaceAll("\"\"", "\""); System.out.print("|" + decoded); if
			 * (!match.group(4).equals(",")) { System.out.println("|"); } }
			 */
			System.out.println(conteudo.toString());
			return conteudo.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	@GET
	@Path(value = "selectByLocationByMaxDate")
	@Produces(MediaType.TEXT_PLAIN)
	public String selectByLocationByMaxDate(@QueryParam("table") String table,@QueryParam("date") String date) {
		try {		
			
			service = AccessTokenStore.authenticate();
			String query = "?sql="
					+ URLEncoder.encode("SELECT  Location, count() FROM "
							+ table + " WHERE Date  <='"+date+"' GROUP BY Location", "UTF-8");			
			this.url = new URL(FUSION_SERVICE_URL + query);
			GDataRequest request = service.getRequestFactory().getRequest(
					RequestType.QUERY, this.url, ContentType.TEXT_PLAIN);			
			
			request.execute();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getResponseStream()));

		

	
			String line;
			StringWriter conteudo = new StringWriter();
			while ((line = br.readLine()) != null) {
				conteudo.append(line + ";");
			}
			/*
			 * String decoded = new String(); Scanner scanner = new
			 * Scanner(request.getResponseStream(), "UTF-8"); while
			 * (scanner.hasNextLine()) {
			 * scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0); MatchResult
			 * match = scanner.match(); String quotedString = match.group(2);
			 * decoded = quotedString == null ? match.group(1) : quotedString
			 * .replaceAll("\"\"", "\""); System.out.print("|" + decoded); if
			 * (!match.group(4).equals(",")) { System.out.println("|"); } }
			 */
			System.out.println(conteudo.toString());
			return conteudo.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
