File generatedDir = new File(basedir, "target/classes/com/leandronunes85/log_format_enforcer/simple/it");
assert generatedDir.isDirectory();

File generatedFile = new File(generatedDir, "LogFormatEnforcer.class");
assert generatedFile.isFile();
