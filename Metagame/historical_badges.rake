require 'csv' 
namespace :historical_badges do
  desc "Esto actualiza los eventos de forma historica "
  task :create, [:file_name] => [:environment] do |t, args|
    #archivo_json = [{...}] # hardcoear ./blabla.json args[:file_name]
    #puts "Args were: #{args[:file_name]}"
	csv_text = File.read(args[:file_name])
	csv = CSV.parse(csv_text, :headers => true,quote_char: "|")
	nLines = csv.size
	index = 0
    csv.each do |act|
      # Obtener el player
	index = index+1
	if index%10000 == 0
    puts "event: #{index} of #{nLines}"
    end
	#puts "time were: #{act["Time"]}"
	fecha = act["Time"]
	#puts "player were: #{act["Player"]}"
	next if act["Player"] == "Anonymous"
	player  = Player.find_or_create_by(email:act["Player"])
	#old_badges = Marshal.load( Marshal.dump(player.badges) )
	old_badges = player.badges.dup
	old_size = old_badges.size
	#puts "old_badges were: #{old_badges.size }"
	#puts "player were: #{player}"
	#puts "proyect were: #{act["Project"] }"
	project = Project.find_by(name:act["Project"])
	#puts "proyect were: #{project }"
	event = act["Event Type"]
	next if act["Project"] == "Null"
	next if event == "Null"
	

	#puts "event were: #{event }"
    count = 1
    
      # Obtenes los demas datos para armar la act
      # Activity
      @activity = Activity.get_activity_type(player,event,project,count)
      #puts "activity were: #{@activity }"
      # Record activity
      player.record_activity(@activity )
      new_badges = player.badges - old_badges
      #puts "old_badges were: #{old_badges.size }"
      #puts "player.badges were: #{player.badges.size }"
      player_size = player.badges.size
      #puts "new_badges were: #{new_badges}"
      #if ! new_badges.empty?
		if player_size>old_size
		#puts "new_badges were"
		new_badges = player.badges.last(player_size-old_size)
        new_badges.map { |b| #puts "Issue time were: #{b.issues.last.created_at}"
        b.issues.last.update(created_at: fecha)
        b.issues.last.save!
        #puts "Issue time were: #{b.issues.last.created_at}" 
        }
      end
      #puts "new_badges were: #{new_badges}"
      # Guardas el player
      player.save!
    end

  end
end

# byebug
