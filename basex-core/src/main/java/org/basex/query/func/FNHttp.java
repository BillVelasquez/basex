package org.basex.query.func;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.iter.*;
import org.basex.query.util.http.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.*;
import org.basex.util.*;

/**
 * HTTP Client Module.
 * @author BaseX Team 2005-14, BSD License
 * @author Rositsa Shadura
 */
public final class FNHttp extends BuiltinFunc {
  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param func function definition
   * @param args arguments
   */
  public FNHttp(final StaticContext sc, final InputInfo info, final Function func,
      final Expr... args) {
    super(sc, info, func, args);
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    checkCreate(qc);

    // get request node
    final ANode request = toNode(exprs[0].item(qc, info));

    // get HTTP URI
    final byte[] href = exprs.length >= 2 ? toToken(exprs[1], qc, true) : null;
    // get parameter $bodies
    ValueBuilder cache = null;
    if(exprs.length == 3) {
      final Iter bodies = exprs[2].iter(qc);
      cache = new ValueBuilder();
      for(Item body; (body = bodies.next()) != null;) cache.add(body);
    }
    // send HTTP request
    return new HTTPClient(info, qc.context.options).sendRequest(href, request, cache);
  }
}
