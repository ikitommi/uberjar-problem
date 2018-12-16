# protocol-bug

This works:

```bash
lein with-profile uberjar/all uberjar
# Compiling protocol-bug.env
# Compiling protocol-bug.layout
# Compiling protocol-bug.middleware.formats
# Compiling protocol-bug.nrepl
# Compiling protocol-bug.middleware
# Compiling protocol-bug.core
# Compiling protocol-bug.handler
# Compiling protocol-bug.routes.home
# Compiling protocol-bug.config
```

This fails:

```bash
docker build .
# Compiling protocol-bug.env
# Compiling protocol-bug.core
# Compiling protocol-bug.nrepl
# Compiling protocol-bug.routes.home
# Compiling protocol-bug.layout
# Compiling protocol-bug.config
# Compiling protocol-bug.middleware.formats
# Compiling protocol-bug.handler
# Compiling protocol-bug.middleware
```

To run in the failing order locally:

```bash
lein with-profile uberjar/fail uberjar
# Compiling protocol-bug.env
# Compiling protocol-bug.core
# Compiling protocol-bug.nrepl
# Compiling protocol-bug.routes.home
# Compiling protocol-bug.layout
# Compiling protocol-bug.config
# Compiling protocol-bug.middleware.formats
# Compiling protocol-bug.handler
# Compiling protocol-bug.middleware
```

To run in manual order (this works with Docker too):

```bash
lein with-profile uberjar/ok uberjar
# Compiling protocol-bug.env
# Compiling protocol-bug.layout
# Compiling protocol-bug.middleware.formats
# Compiling protocol-bug.nrepl
# Compiling protocol-bug.middleware
# Compiling protocol-bug.core
# Compiling protocol-bug.handler
# Compiling protocol-bug.routes.home
# Compiling protocol-bug.config
```