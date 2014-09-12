package com.ggfos.util


trait ImplicitCookies extends WeekBooleanCookies {
  protected implicit def anyToOption[R](x: R)(implicit ev: Null <:< R) = Option(x)
  type Str = String
}
