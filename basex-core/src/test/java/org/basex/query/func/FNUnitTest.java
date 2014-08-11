package org.basex.query.func;

import static org.basex.query.func.Function.*;

import org.basex.query.util.*;
import org.basex.query.*;
import org.junit.*;

/**
 * This class tests the functions of the Unit Module.
 *
 * @author BaseX Team 2005-14, BSD License
 * @author Christian Gruen
 */
public final class FNUnitTest extends AdvancedQueryTest {
  /** Test method. */
  @Test
  public void faill() {
    error(_UNIT_FAIL.args("1"), Err.UNIT_MESSAGE_X);
  }

  /** Test method. */
  @Test
  public void assrt() {
    query(_UNIT_ASSERT.args("1"), "");
    query(_UNIT_ASSERT.args("(<a/>,<b/>)"), "");
    error(_UNIT_ASSERT.args("()"), Err.UNIT_ASSERT);
    error(_UNIT_ASSERT.args("()", "X"), Err.UNIT_MESSAGE_X);
  }

  /** Test method. */
  @Test
  public void assertEquals() {
    query(_UNIT_ASSERT_EQUALS.args("1", "1"), "");
    error(_UNIT_ASSERT_EQUALS.args("1", "2"), Err.UNIT_ASSERT_EQUALS_X_X_X);
  }
}
