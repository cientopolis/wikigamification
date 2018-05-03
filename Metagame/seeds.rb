# It is created in this file the

#Projects
projects= [
"Category:Aquaculture",
"Category:Beekeeping",
"Category:Cannabis cultivation",
"Category:Commercial farming",
"Category:Dairy farming",
"Category:Domesticated animals",
"Category:Forestry",
"Category:Hemp agriculture",
"Category:Livestock",
"Category:Orchards",
"Category:Organic farming",
"Category:Permaculture",
"Category:Pig farming",
"Category:Poultry farming",
"Category:Sustainable agriculture",
"Category:Viticulture"
]  

projects.each do |pname|
proje = Project.create(name:pname)

#Tutorial badges
Badge.create(name:"i-was-here",project_id:proje.id,points:1,badge_type:"login")
Badge.create(name:"welcome-back",project_id:proje.id,points:2,badge_type:"login")
(1..10).each do |i|
	#Unit of Work badges
	Badge.create(name:"#{i}-contribution",points:(2*i),project_id:proje.id,badge_type:"contribution")

	#Back and forth badges
	Badge.create(name:"#{i}-reinforcement",points:(10*i),project_id:proje.id,badge_type:"reinforcement")

	#Shout	 out	 loud badges
	Badge.create(name:"Share-on-#{i}",points:(1*i),project_id:proje.id,badge_type:"dissemination")
	

end
puts "Here's your #{pname} App token: #{proje.token_value}"
end
# #Ranks
PlayerRank.create(name:"Visitor",type:"Visitor")
PlayerRank.create(name:"Explorer",type:"Explorer")
PlayerRank.create(name:"Citizen Scientist",type:"CitizenScientist")
PlayerRank.create(name:"Prolific Citizen Scientist",type:"ProlificCitizenScientist")
PlayerRank.create(name:"Commited Citizen Scientist",type:"CommitedCitizenScientist")
PlayerRank.create(name:"Visionary",type:"VisionaryCitizenScientist")

#Player
player = Player.create(email:"santiagopravisani@gmail.com")


