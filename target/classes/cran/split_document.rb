docs_path = File.join(File.dirname(__FILE__), 'cran.all.1400')
docs = File.read(docs_path)
docs.gsub!("\n", " ").gsub!("  ", " ")
docs = docs.split(".I")
i = 0
docs.each do |doc|
    m = ((/.W.*/).match doc)
    m = m[0] if m
    if !m.nil?
        m = m.gsub(".W ","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "")
    end
    out_file = File.new("parsed/docs/" + i.to_s, "w")
    out_file.puts(m)
    out_file.close
    i = i+1
end

docs_path = File.join(File.dirname(__FILE__), 'cran.qry')
docs = File.read(docs_path)
docs.gsub!("\r\n", " ").gsub!("  ", " ")
docs = docs.split(".I")
i = 0
docs.each do |doc|
    m = ((/.W.*/).match doc)
    m = m[0] if m
    if !m.nil?
        m = m.gsub(".W ","").gsub("-"," ").gsub("'s","").gsub(". ","").gsub("*", "").gsub("?", "")
    end
    out_file = File.new("parsed/query/" + i.to_s, "w")
    out_file.puts(m)
    out_file.close
    i = i+1
end

docs_path = File.join(File.dirname(__FILE__), 'cranqrel')
File.open(docs_path).each do |line|
  line = line.split(" ")
  line.insert(1, 0)
  a = line[0]
  line[3] = 1 if line[3] == "-1"
  for i in 1..3
    a = a + " " + line[i].to_s
  end
  open('parsed_answer', 'a') { |f|
    f.puts a
  }
end