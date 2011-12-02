package org.structr.core;

import org.structr.common.SecurityContext;

/**
 * A transformation that can be applied to an object in the
 * presence of a security context.
 *
 * @author Christian Morgner
 */

public interface Transformation<T> {
	public void apply(SecurityContext securityContext, T obj);
}
