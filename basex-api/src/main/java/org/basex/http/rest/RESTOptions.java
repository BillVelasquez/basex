package org.basex.http.rest;

import static org.basex.http.rest.RESTText.*;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.basex.http.*;
import org.basex.io.serial.*;
import org.basex.util.*;

/**
 * <p>This servlet receives and processes OPTIONS request for CoRS preflight check.</p>
 *
 * @author Creativos Digitales http://creativosdigitales.co, BSD License
 * @author William Velasquez
 */
final class RESTOptions {
  /** Private constructor. */
  private RESTOptions() { }

  /**
   * Creates and returns a OPTIONS command for CoRS support
   * RFC: https://tools.ietf.org/html/rfc7231#section-4.3.7
   * Documentation of CoRS at https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/OPTIONS
   * @param session REST session
   * @return code
   * @throws IOException I/O exception
   */
  public static RESTCmd get(final RESTSession session) throws IOException {
    final HTTPConnection conn = session.conn;
    final SerializerOptions sopts = conn.sopts();

    String requestedMethod = conn.req.getHeader( "Access-Control-Request-Method" );

    if( requestedMethod == null ) {
      // Simple Request
      conn.res.setHeader( "Allow", "OPTIONS, GET, POST, PUT, DELETE" ) ;
//      conn.res.setHeader("Access-Control-Allow-Origin", "*");
     } else {
      // Prefligthed Request
//      conn.res.setHeader("Access-Control-Allow-Origin", "*");
      conn.res.setHeader( "Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE" ) ;
      conn.res.setHeader( "Access-Control-Allow-Headers",  "Content-Type" );
     }
    conn.res.setStatus(200);

    return null;

  }
}
