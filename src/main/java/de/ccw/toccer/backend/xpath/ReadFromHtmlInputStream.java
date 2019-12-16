package de.ccw.toccer.backend.xpath;

import java.io.IOException;
import java.io.InputStream;

public class ReadFromHtmlInputStream extends InputStream {

	private static final byte[] SEARCH_BYTES = "html".getBytes();

	final InputStream inputStream;

	private boolean found;

	private byte[] buffer2;

	private boolean useBuffer2;

	private boolean isJson;

	public ReadFromHtmlInputStream(InputStream inputStream, boolean isJson) {
		this.inputStream = inputStream;
		this.isJson = isJson;
	}

	@Override
	public int read() throws IOException {
		if (isJson) {
			return inputStream.read();
		}

		while (!found) {
			final byte[] buffer = new byte[1];
			final int result = inputStream.read(buffer, 0, 1);

			if (result == -1) {
				return -1;
			}

			if (buffer[0] == '<') {
				buffer2 = new byte[SEARCH_BYTES.length];
				final int result2 = inputStream.read(buffer2, 0, buffer2.length);

				if (result2 == -1) {
					return -1;
				}

				boolean ok = true;
				for (int i = 0; i < buffer2.length; i++) {
					if (buffer2[i] != SEARCH_BYTES[i]) {
						ok = false;
					}
					if (buffer2[i] == -1) {
						return -1;
					}
				}

				if (ok) {
					buffer2 = new byte[] { '<', buffer2[0], buffer2[1], buffer2[2], buffer2[3] };
					found = true;
					useBuffer2 = true;
				}
			}
		}

		if (useBuffer2) {
			if (buffer2.length == 1) {
				useBuffer2 = false;
				return buffer2[0];
			} else {
				final byte[] newBuffer2 = new byte[buffer2.length - 1];
				System.arraycopy(buffer2, 1, newBuffer2, 0, newBuffer2.length);
				final byte sending = buffer2[0];
				buffer2 = newBuffer2;
				return sending;
			}
		} else {
			return inputStream.read();
		}
	}
}
