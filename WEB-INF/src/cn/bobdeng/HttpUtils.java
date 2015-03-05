package cn.bobdeng;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	private String url;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private List<Header> headers = new ArrayList<Header>();
	private List<FormFile> files = new ArrayList<FormFile>();

	public HttpUtils setUrl(String url) {
		System.out.println(url);
		this.url = url;
		return this;
	}

	public HttpUtils addFile(String name, String file) {
		files.add(new FormFile().setName(name).setFileName(file));
		return this;
	}

	public HttpUtils addParam(final String name, final String value) {
		params.add(new NameValuePair() {

			@Override
			public String getValue() {
				// TODO Auto-generated method stub
				return value;
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return name;
			}
		});
		return this;
	}

	public HttpUtils addHeader(final String name, final String value) {
		headers.add(new Header() {

			@Override
			public String getValue() {
				// TODO Auto-generated method stub
				return value;
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return name;
			}

			@Override
			public HeaderElement[] getElements() throws ParseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
		return this;
	}

	public String httpsUpload() throws Exception {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				builder.build());
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder reqBuilder = MultipartEntityBuilder.create();
		//reqBuilder.setCharset(Charset.forName("utf-8"));
		for (FormFile ff : this.files) {
			reqBuilder.addPart(ff.getName(),
					new FileBody(new File(ff.getFileName())));
		}
		ContentType contentType=ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		for (NameValuePair nv : this.params) {
			reqBuilder.addPart(nv.getName(),
					new StringBody(nv.getValue(),
							contentType));
		}
		for (Header h : headers) {
			post.addHeader(h);
		}
		post.setEntity(reqBuilder.build());
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					System.out.println(EntityUtils.toString(response
							.getEntity()));
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		return httpclient.execute(post, responseHandler);
	}

	public String httpsPost() throws Exception {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				builder.build());
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
		for (Header h : headers) {
			post.addHeader(h);
		}
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					System.out.println(EntityUtils.toString(response
							.getEntity()));
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		return httpclient.execute(post, responseHandler);
	}

	public byte[] httpsDown() throws Exception {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				builder.build());
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		HttpGet post = new HttpGet(url);
		ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {

			public byte[] handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toByteArray(entity) : null;
			}

		};
		return httpclient.execute(post, responseHandler);
	}

	public String httpsGet() throws Exception {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				builder.build());
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		HttpGet get = new HttpGet(url);
		for (Header h : headers) {
			get.addHeader(h);
		}
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					System.out.println(EntityUtils.toString(response.getEntity()));
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		return httpclient.execute(get, responseHandler);
	}

	public byte[] httpGet() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {

			public byte[] handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toByteArray(entity)
							: null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		return httpclient.execute(get, responseHandler);
	}

	public static void main(String[] args) throws MalformedURLException,
			IOException, URISyntaxException, AWTException {
		// 此方法仅适用于JdK1.6及以上版本
		Desktop.getDesktop().browse(new URL("https://twitter.com/fangshimin/status/572617633939365888").toURI());
		Robot robot = new Robot();
		robot.delay(10000);
		Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		int width = (int) d.getWidth();
		int height = (int) d.getHeight();
		// 最大化浏览器
		robot.keyRelease(KeyEvent.VK_F11);
		robot.delay(2000);
		Image image = robot.createScreenCapture(new Rectangle(0, 0, width,
				height));
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		// 保存图片
		ImageIO.write(bi, "jpg", new File("url.jpg"));
	}
}
